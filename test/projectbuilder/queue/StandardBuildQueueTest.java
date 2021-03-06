/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbuilder.queue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import static org.junit.Assert.*;
import projectbuilder.project.Project;
import projectbuilder.trigger.JobNotification;

/**
 *
 * @author lachlan
 */
public class StandardBuildQueueTest {

    @Test
    public void testPushJob() {
        final Project project = null;
        JobNotification notification = null;

        final CountDownLatch testLatch = new CountDownLatch(1);

        BuildQueue queue = new StandardBuildQueue();
        queue.addProcessor(new BuildProcessor() {

            @Override
            public void process(BuildRequest br) {
                if (br.getProject() == project)
                    testLatch.countDown();
            }
        });

        queue.pushJob(project, notification);

        try {
            boolean success = testLatch.await(5, TimeUnit.SECONDS);

            if (!success)
                fail("Did not succeed in 1 second");
        } catch (InterruptedException ie) {
            fail("Interrupted");
        }
    }
}
