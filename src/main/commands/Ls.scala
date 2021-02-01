package main.commands

import main.files.DirEntry
import main.filesystem.State

class Ls extends Command {
    override def apply(state: State): State = {
        val contents = state.wd.contents
        state.setMsg(createOutput(contents))
    }

    def createOutput(contents: List[DirEntry]): String = {
        if (contents.isEmpty) ""
        else {
            val entry = contents.head
             entry.name + "[" + entry.getType + "]" + "\n" + createOutput(contents.tail)
        }
    }
}
