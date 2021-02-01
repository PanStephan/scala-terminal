package main.commands

import main.files.{DirEntry, File}
import main.filesystem.State

class Touch(name: String) extends CreateEntry(name) {
    override def createEntry(state: State): DirEntry = {
        File.empty(state.wd.path, name)
    }
}
