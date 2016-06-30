/*
 * Copyright 2006-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.docker.command;

import com.consol.citrus.context.TestContext;
import com.consol.citrus.docker.client.DockerClient;
import com.consol.citrus.docker.message.DockerMessageHeaders;
import com.consol.citrus.exceptions.CitrusRuntimeException;
import com.consol.citrus.util.FileUtils;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * @author Christoph Deppisch
 * @since 2.4
 */
public class ImageBuild extends AbstractDockerCommand<BuildResponseItem> {

    /**
     * Default constructor initializing the command name.
     */
    public ImageBuild() {
        super("docker:build");
    }

    @Override
    public void execute(DockerClient dockerClient, TestContext context) {
        BuildImageCmd command = dockerClient.getEndpointConfiguration().getDockerClient().buildImageCmd();

        if (hasParameter("no-cache")) {
            command.withNoCache(Boolean.valueOf(getParameter("no-cache", context)));
        }

        if (hasParameter("basedir")) {
            try {
                command.withBaseDirectory(FileUtils.getFileResource(getParameter("basedir", context), context).getFile());
            } catch (IOException e) {
                throw new CitrusRuntimeException("Failed to access Dockerfile base directory", e);
            }
        }

        if (hasParameter("dockerfile")) {
            try {
                if (getParameters().get("dockerfile") instanceof Resource) {
                    command.withDockerfile(((Resource)getParameters().get("dockerfile")).getFile());
                } else {
                    command.withDockerfile(FileUtils.getFileResource(getParameter("dockerfile", context), context).getFile());
                }
            } catch (IOException e) {
                throw new CitrusRuntimeException("Failed to read Dockerfile", e);
            }
        }

        if (hasParameter("quiet")) {
            command.withNoCache(Boolean.valueOf(getParameter("quiet", context)));
        }

        if (hasParameter("remove")) {
            command.withRemove(Boolean.valueOf(getParameter("remove", context)));
        }

        if (hasParameter("tag")) {
            command.withTag(getParameter("tag", context));
        }

        BuildImageResultCallback imageResult = new BuildImageResultCallback() {
            @Override
            public void onNext(BuildResponseItem item) {
                super.onNext(item);
                setCommandResult(item);
            }
        };

        command.exec(imageResult);
        String imageId = imageResult.awaitImageId();

        context.setVariable(DockerMessageHeaders.IMAGE_ID, imageId);
    }

    /**
     * Sets the noCache parameter.
     * @param noCache
     * @return
     */
    public ImageBuild noCache(Boolean noCache) {
        getParameters().put("no-cache", noCache);
        return this;
    }

    /**
     * Sets the basedir parameter.
     * @param basedir
     * @return
     */
    public ImageBuild basedir(String basedir) {
        getParameters().put("basedir", basedir);
        return this;
    }

    /**
     * Sets the tag parameter.
     * @param tag
     * @return
     */
    public ImageBuild tag(String tag) {
        getParameters().put("tag", tag);
        return this;
    }

    /**
     * Sets the remove parameter.
     * @param remove
     * @return
     */
    public ImageBuild remove(Boolean remove) {
        getParameters().put("remove", remove);
        return this;
    }

    /**
     * Sets the quiet parameter.
     * @param quiet
     * @return
     */
    public ImageBuild quiet(Boolean quiet) {
        getParameters().put("quiet", quiet);
        return this;
    }

    /**
     * Sets the docker file parameter.
     * @param filePath
     * @return
     */
    public ImageBuild dockerFile(String filePath) {
        getParameters().put("dockerfile", filePath);
        return this;
    }

    /**
     * Sets the docker file parameter.
     * @param fileResource
     * @return
     */
    public ImageBuild dockerFile(Resource fileResource) {
        getParameters().put("dockerfile", fileResource);
        return this;
    }
}
