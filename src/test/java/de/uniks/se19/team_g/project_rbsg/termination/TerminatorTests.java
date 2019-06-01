package de.uniks.se19.team_g.project_rbsg.termination;

import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.LogoutManager;
import javafx.beans.InvalidationListener;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
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

    static class TestLogoutManager implements LogoutManager, Terminable {
        public int logoutCallCount = 0;

        public TestLogoutManager(@NonNull final Terminator terminator) {
            terminator.register(this);
        }

        @Override
        public void logout() {
            logoutCallCount++;
        }

        @Override
        public void terminate() {
            logout();
        }
    }


    @Test
    public void testTerminateRootController() {
        final Terminator terminator = new Terminator();
        final SceneManager sceneManager = new SceneManager(terminator);
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

        terminator.terminate();

        Assert.assertNull(sceneManager.getRootController());
        Assert.assertTrue(secondTerminableRootController.hasBeenTerminated);
    }


    @Test
    public void testRegisterTerminable() {
        final Terminator terminator = new Terminator();

        final TestTerminable firstTerminable = new TestTerminable(terminator);
        final TestTerminable secondTerminable = new TestTerminable(terminator);

        final ObservableSet<Terminable> registeredTerminables = terminator.getRegisteredTerminables();

        Assert.assertTrue(registeredTerminables.isEmpty());
        Assert.assertFalse(firstTerminable.hasBeenTerminated);
        Assert.assertFalse(secondTerminable.hasBeenTerminated);

        firstTerminable.register();

        Assert.assertTrue(registeredTerminables.contains(firstTerminable));
        Assert.assertFalse(registeredTerminables.contains(secondTerminable));

        secondTerminable.register();

        Assert.assertTrue(registeredTerminables.contains(firstTerminable));
        Assert.assertTrue(registeredTerminables.contains(secondTerminable));

        firstTerminable.unregister();

        Assert.assertFalse(registeredTerminables.contains(firstTerminable));
        Assert.assertTrue(registeredTerminables.contains(secondTerminable));

        terminator.terminate();

        Assert.assertTrue(registeredTerminables.isEmpty());

        Assert.assertFalse(firstTerminable.hasBeenTerminated);
        Assert.assertTrue(secondTerminable.hasBeenTerminated);
    }

    @Test
    public void testLogoutCall() {
        final Terminator terminator = new Terminator();
        final TestLogoutManager testLogoutManager = new TestLogoutManager(terminator);

        Assert.assertEquals(0, testLogoutManager.logoutCallCount);

        terminator.terminate();

        Assert.assertEquals(1, testLogoutManager.logoutCallCount);
    }
}
