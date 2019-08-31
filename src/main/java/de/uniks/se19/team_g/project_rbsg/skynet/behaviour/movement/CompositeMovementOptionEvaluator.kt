package de.uniks.se19.team_g.project_rbsg.skynet.behaviour.movement

import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.Tour
import org.springframework.lang.NonNull

class CompositeMovementOptionEvaluator : MovementOptionEvaluator {

    override fun compare(@NonNull first: MovementOption,
                         @NonNull second: MovementOption): Int {
        return 0
    }

    private fun compareMovementOptions(@NonNull first: MovementOption,
                                       @NonNull second: MovementOption) {

    }

    private fun compareTours(@NonNull first: Tour,
                             @NonNull second: Tour) {

    }

    private fun compareEnemies(@NonNull first: Enemy,
                               @NonNull second: Enemy) {

    }
}
