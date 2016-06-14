package no.nav.sbl.tekster;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Goal which touches a timestamp file.
 */
@Mojo(name = "merge", defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class MyMojo extends AbstractMojo {
    @Parameter(defaultValue = "${basedir}", property = "basedir", required = true)
    private File basedir;
    @Parameter(defaultValue = "${tekster}", property = "tekster", required = true)
    private File tekster;
    @Parameter(defaultValue = "${project.build.outputDirectory}", property = "outputDir", required = true)
    private File outputDirectory;

    public void execute() throws MojoExecutionException {
        try {
            Files.list(tekster.toPath()).forEach(path -> {
                Map<String, List<FilMerger.Tekst>> stringListMap = FilMerger.mergeFiler(path.toFile().getAbsolutePath(), "");
                stringListMap.entrySet().forEach(es -> {
                    Path newFile = Paths.get(outputDirectory.getAbsolutePath(), path.toFile().getName() + "_" + es.getKey() + ".properties");
                    List<String> values = es.getValue().stream().map(tekst -> tekst.key + "=" + tekst.value).collect(Collectors.toList());
                    try {
                        outputDirectory.mkdirs();
                        Files.write(newFile, values);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            });

        } catch (IOException e) {
            throw new MojoExecutionException("Kunne ikke lese input-dir");
        }
    }
}
