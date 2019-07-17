package de.uniks.se19.team_g.project_rbsg.ingame.battlefield;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class MovementManagerTest {

    @Test
    public void testEvaluationOfEntity() {
        final MovementEvaluator movementEvaluator = mock(MovementEvaluator.class);
        MovementManager sut = new MovementManager(movementEvaluator);

        Unit unit = new Unit("1");
        Unit otherUnit = new Unit("2");
        Cell firstStart = new Cell("firstStart");
        Cell secondStart = new Cell( "secondStart");
        Cell target1 = new Cell("target1");
        Cell target2 = new Cell("target2");

        unit.setPosition(firstStart);

        final Tour tourToTarget1 = new Tour();
        Map<Cell, Tour> toursFromFirst = Collections.singletonMap(target1, tourToTarget1);
        final Tour tourToTarget2 = new Tour();
        Map<Cell, Tour> toursFromSecond = Collections.singletonMap(target2, tourToTarget2);
        Map<Cell, Tour> caged = Collections.emptyMap();

        //noinspection unchecked
        when(movementEvaluator.getAllowedTours(unit))
                .thenReturn(
                    toursFromFirst,
                    toursFromSecond
                );
        when(movementEvaluator.getAllowedTours(otherUnit)).thenReturn(caged);

        Tour answer;
        answer = sut.getTour(unit, target1);
        verify(movementEvaluator).getAllowedTours(unit);
        assertSame(tourToTarget1, answer);
        // target2 not set in toursFromFirst, i expect that the first answer is reused
        answer = sut.getTour(unit, target2);
        verifyNoMoreInteractions(movementEvaluator);
        assertNull(answer);

        assertNull(sut.getTour(otherUnit, target1));
        verify(movementEvaluator).getAllowedTours(otherUnit);
        // no changes to another units data
        assertSame(tourToTarget1, sut.getTour(unit, target1));
        verifyNoMoreInteractions(movementEvaluator);

        // changed position should invalidate cached tours
        unit.setPosition(secondStart);
        verifyNoMoreInteractions(movementEvaluator);
        clearInvocations(movementEvaluator);
        // second call to evaluator for unit
        // target1 not set in toursFromSecond
        answer = sut.getTour(unit, target1);
        verify(movementEvaluator).getAllowedTours(unit);
        assertNull(answer);
        assertSame(tourToTarget2, sut.getTour(unit, target2));
    }
}
