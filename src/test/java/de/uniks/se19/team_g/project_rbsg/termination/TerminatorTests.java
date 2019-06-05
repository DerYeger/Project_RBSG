package de.uniks.se19.team_g.project_rbsg.termination;

import de.uniks.se19.team_g.project_rbsg.SceneManager;
import javafx.collections.ObservableSet;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.lang.NonNull;

/**
 * @author Jan Müller
 */
public class TerminatorTests {

    static class TestTerminableRootController implements RootController, Terminable {
        boolean hasBeenTerminated = false;

        @NonNull
        private final SceneManager sceneManager;

        TestTerminableRootController(@NonNull final SceneManager sceneManager) {
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
        boolean hasBeenTerminated = false;

        TestTerminable(@NonNull final Terminator terminator) {
            this.terminator = terminator;
        }

        void register() {
            terminator.register(this);
        }

        void unregister() {
            terminator.unregister(this);
        }

        @Override
        public void terminate() {
            hasBeenTerminated = true;
        }
    }

    @Test
    public void testTerminateRootController() {
        final SceneManager sceneManager = new SceneManager();
        final Terminator terminator = new Terminator();
        final TestTerminableRootController firstTerminableRootController = new TestTerminableRootController(sceneManager);
        final TestTerminableRootController secondTerminableRootController = new TestTerminableRootController(sceneManager);

        terminator.register(sceneManager);

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
}
