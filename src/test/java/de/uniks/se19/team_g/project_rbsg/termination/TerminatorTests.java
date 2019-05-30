package de.uniks.se19.team_g.project_rbsg.termination;

import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.ILogoutManager;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.lang.NonNull;

/**
 * @author Jan Müller
 */
public class TerminatorTests {

    static class TestTerminableRootController implements RootController, Terminable {
        public boolean hasBeenTerminated = false;

        @NonNull
        private final SceneManager sceneManager;

        public TestTerminableRootController(@NonNull final SceneManager sceneManager) {
            this.sceneManager = sceneManager;
        }
        @Override
        public void setAsRootController() {
            sceneManager.setRootController(this);
        }

        @Override
        public void terminate() {
            hasBeenTerminated = true;
        }
    }

    static class TestTerminable implements Terminable {
        private Terminator terminator;
        public boolean hasBeenTerminated = false;

        public TestTerminable(@NonNull final Terminator terminator) {
            this.terminator = terminator;
        }

        public void register() {
            terminator.register(this);
        }

        public void unregister() {
            terminator.unregister(this);
        }

        @Override
        public void terminate() {
            hasBeenTerminated = true;
        }
    }

    static class TestLogoutManager implements ILogoutManager {
        public int logoutCallCount = 0;

        @Override
        public void logout() {
            logoutCallCount++;
            System.out.println("logged out");
        }
    }


    @Test
    public void testTerminateRootController() {
        final SceneManager sceneManager = new SceneManager();
        final Terminator terminator = new Terminator(sceneManager, new TestLogoutManager());
        final TestTerminableRootController firstTerminableRootController = new TestTerminableRootController(sceneManager);
        final TestTerminableRootController secondTerminableRootController = new TestTerminableRootController(sceneManager);

        Assert.assertNull(sceneManager.getRootController());

        firstTerminableRootController.setAsRootController();

        Assert.assertEquals(firstTerminableRootController, sceneManager.getRootController());

        Assert.assertFalse(firstTerminableRootController.hasBeenTerminated);
        Assert.assertFalse(secondTerminableRootController.hasBeenTerminated);

        secondTerminableRootController.setAsRootController();

        Assert.assertEquals(secondTerminableRootController, sceneManager.getRootController());
        Assert.assertTrue(firstTerminableRootController.hasBeenTerminated);
        Assert.assertFalse(secondTerminableRootController.hasBeenTerminated);

        terminator.terminateRootController();

        Assert.assertNull(sceneManager.getRootController());
        Assert.assertTrue(secondTerminableRootController.hasBeenTerminated);
    }


    @Test
    public void testRegisterTerminable() {
        final Terminator terminator = new Terminator(new SceneManager(), new TestLogoutManager());

        final TestTerminable firstTerminable = new TestTerminable(terminator);
        final TestTerminable secondTerminable = new TestTerminable(terminator);

        Assert.assertTrue(terminator.getRegisteredTerminables().isEmpty());
        Assert.assertFalse(firstTerminable.hasBeenTerminated);
        Assert.assertFalse(secondTerminable.hasBeenTerminated);

        firstTerminable.register();

        Assert.assertTrue(terminator.getRegisteredTerminables().contains(firstTerminable));
        Assert.assertFalse(terminator.getRegisteredTerminables().contains(secondTerminable));

        secondTerminable.register();

        Assert.assertTrue(terminator.getRegisteredTerminables().contains(firstTerminable));
        Assert.assertTrue(terminator.getRegisteredTerminables().contains(secondTerminable));

        firstTerminable.unregister();

        Assert.assertFalse(terminator.getRegisteredTerminables().contains(firstTerminable));
        Assert.assertTrue(terminator.getRegisteredTerminables().contains(secondTerminable));

        terminator.terminateRegistered();

        Assert.assertTrue(terminator.getRegisteredTerminables().isEmpty());

        Assert.assertFalse(firstTerminable.hasBeenTerminated);
        Assert.assertTrue(secondTerminable.hasBeenTerminated);
    }

    @Test
    public void testLogoutCall() {
        final TestLogoutManager testLogoutManager = new TestLogoutManager();
        final Terminator terminator = new Terminator(new SceneManager(), testLogoutManager);

        Assert.assertEquals(0, testLogoutManager.logoutCallCount);

        terminator.logoutUser();

        Assert.assertEquals(1, testLogoutManager.logoutCallCount);
    }
}
