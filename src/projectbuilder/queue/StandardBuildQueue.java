/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbuilder.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import projectbuilder.project.Project;
import projectbuilder.trigger.JobNotification;

/**
 *
 * @author lachlan
 */
public class StandardBuildQueue implements BuildQueue {

    private final List<BuildProcessor> processors;
    private final ExecutorService processorService;

    public StandardBuildQueue() {
        processors = new ArrayList<>();
        processorService = Executors.newCachedThreadPool();
    }

    @Override
    public void addProcessor(BuildProcessor processor) {
        processors.add(processor);
    }

    @Override
    public void removeProcessor(BuildProcessor processor) {
        processors.remove(processor);
    }

    @Override
    public void pushJob(final Project project, JobNotification notification) {
        for (final BuildProcessor processor : processors) {
            processorService.submit(new Runnable() {

                @Override
                public void run() {
                    processor.process(project);
                }
            });
        }
    }
}
