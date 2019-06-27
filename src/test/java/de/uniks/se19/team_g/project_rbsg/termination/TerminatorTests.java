package de.uniks.se19.team_g.project_rbsg.termination;

import javafx.collections.ObservableSet;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.lang.NonNull;

/**
 * @author Jan Müller
 */
public class TerminatorTests {

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
    public void testTermination() {
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
