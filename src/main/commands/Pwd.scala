package main.commands

import main.filesystem.State

class Pwd extends Command {
    override def apply(state: State): State =
        state.setMsg(state.wd.path)
}
