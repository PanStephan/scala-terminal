package main.commands

import main.filesystem.State

class Cat(filename: String) extends Command {

    override def apply(state: State): State = {
        val wd = state.wd
        val dirEntry = wd.findEntry(filename)

        if (dirEntry == null || !dirEntry.isFile)
            state.setMsg(filename + " : no such file")
        else
            state.setMsg(dirEntry.asFile.content)
    }

}
