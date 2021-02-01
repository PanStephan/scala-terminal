package main.commands

import main.files.{DirEntry, Directory}
import main.filesystem.State

class Mkdir(name: String) extends CreateEntry(name) {
    override def createEntry(state: State): DirEntry = {
        Directory.empty(state.wd.path, name)
    }
}
