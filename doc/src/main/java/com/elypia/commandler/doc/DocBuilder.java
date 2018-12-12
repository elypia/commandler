package com.elypia.commandler.doc;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.doc.annotations.*;
import com.elypia.commandler.doc.entities.AppData;
import com.elypia.commandler.metadata.ModuleData;
import org.apache.velocity.*;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.jsoup.Jsoup;
import org.slf4j.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class DocBuilder {

    private static final Logger logger = LoggerFactory.getLogger(DocBuilder.class);

    private VelocityEngine engine;

    /**
     * The modules parsed as {@link ModuleData}.
     */
    private ModulesContext context;

    /**
     * All documentation specific data to help generate
     * webpages.
     *
     * This mostly comprises data that might not be required by
     * {@link Commandler} at runtime, but can be useful for
     * the application documentation.
     */
    private AppData data;

    private MarkdownParser parser;

    public DocBuilder() {
        this(new ModulesContext());
    }

    public DocBuilder(ModulesContext context) {
        this.context = context;
        parser = new MarkdownParser(TablesExtension.create());

        Properties velocityProperties = new Properties();
        velocityProperties.setProperty(VelocityEngine.RESOURCE_LOADER, "class");
        velocityProperties.setProperty("class.resource.loader.class", ClasspathResourceLoader.class.getName());

        engine = new VelocityEngine(velocityProperties);
        engine.init();
    }

    public void build() throws IOException {
        build("./build/docs/commandlerdoc/");
    }

    public void build(String path) throws IOException {
        Objects.requireNonNull(path);

        File file = new File(path);

        if (file.exists() && !file.isDirectory())
            throw new IllegalArgumentException("Path must be a directory.");

        Template template = engine.getTemplate("template.vm", "utf-8");
        Map<String, List<ModuleData>> groups = context.getGroups(false);

        VelocityContext globalContext = new VelocityContext();
        globalContext.put("utils", new DocUtils());
        globalContext.put("data", data);
        globalContext.put("parser", parser);
        globalContext.put("modules", context.getModules(false));
        globalContext.put("groups", groups);
        globalContext.put("example_class", Example.class);
        globalContext.put("icon_class", Icon.class);

        VelocityContext allCommandsContext = new VelocityContext(globalContext);
        allCommandsContext.put("page_name", "All Commands");
        allCommandsContext.put("commands", context.getCommands(false));
        allCommandsContext.put("content", "all_commands_template.vm");

        if (data.getReadme() != null) {
            VelocityContext homeContext = new VelocityContext(globalContext);
            homeContext.put("page_name", "Home");
            homeContext.put("content", parser.parseFile(data.getReadme()));
            outputFile(file.getAbsoluteFile(), "index", template, homeContext);

            outputFile(file.getAbsoluteFile(), "all-commands", template, allCommandsContext);
        } else {
            globalContext.put("no_readme", true);
            outputFile(file.getAbsoluteFile(), "index", template, allCommandsContext);
        }

        for (var group : groups.entrySet()) {
            String groupName = group.getKey();
            List<ModuleData> modules = group.getValue();
            String outputName = "groups/" + DocUtils.toOutput(groupName);

            VelocityContext groupContext = new VelocityContext(globalContext);
            groupContext.put("page_name", "Group | " + groupName);
            groupContext.put("group_name", groupName);
            groupContext.put("group_modules", modules);
            groupContext.put("content", "group_template.vm");

            outputFile(file.getAbsoluteFile(), outputName, template, groupContext);
        }

        for (ModuleData module : context.getModules()) {
            if (!module.isPublic())
                continue;

            Module moduleAnno = module.getAnnotation();
            String moduleName = moduleAnno.id();
            String outputName = "modules/" + DocUtils.toOutput(moduleName);

            VelocityContext moduleContext = new VelocityContext(globalContext);
            moduleContext.put("page_name", "Module | " + moduleName);
            moduleContext.put("module", module);
            moduleContext.put("module_anno", moduleAnno);
            moduleContext.put("module_id", moduleAnno.id());
            moduleContext.put("module_group", moduleAnno.group());
            moduleContext.put("commands", module.getPublicCommands());
            moduleContext.put("content", "module_template.vm");

            outputFile(file.getAbsoluteFile(), outputName, template, moduleContext);
        }

        copyFiles(path, "/include");
    }

    private void outputFile(File file, String outputName, Template template, VelocityContext velocityContext) throws IOException {
        String writePath = file.getAbsolutePath() + "/" + outputName + ".html";
        File toWrite = new File(writePath);
        toWrite.getParentFile().mkdirs();

        try (StringWriter stringWriter = new StringWriter()) {
            try (FileWriter fileWriter = new FileWriter(toWrite)) {
                template.merge(velocityContext, stringWriter);

                String cleanHtml = Jsoup.parse(stringWriter.toString()).html();
                fileWriter.write(cleanHtml);
            }
        }
    }

    private void copyFiles(String ouput, String path) {
        copyFiles(ouput, path, path);
    }

    private void copyFiles(String output, String path, String start) {
        try (InputStream inputStream = this.getClass().getResourceAsStream(path)) {
            if (inputStream == null)
                return;

            if (path.matches(".+\\..+")) {
                if (!output.endsWith("/"))
                    output += "/";

                File file = new File(output + path.substring(start.length() + 1));
                file.mkdirs();

                Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                StringBuilder builder = new StringBuilder();

                int c;
                while ((c = inputStream.read()) != -1)
                    builder.append((char)c);

                for (String string : builder.toString().split("\\s*\n\\s*"))
                    copyFiles(output, path + File.separator + string, start);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ModulesContext getContext() {
        return context;
    }

    public DocBuilder setContext(ModulesContext context) {
        this.context = context;
        return this;
    }

    public AppData getData() {
        return data;
    }

    public DocBuilder setData(AppData data) {
        this.data = data;
        return this;
    }
}