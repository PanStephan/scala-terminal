package main.commands

import main.filesystem.State

class UnknownCmd extends Command {
    override def apply(state: State): State =
        state.setMsg("Command not found")
}
