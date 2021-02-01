package main.commands

import main.files.{DirEntry, Directory}
import main.filesystem.State

abstract class CreateEntry(name: String) extends Command {

    override def apply(state: State): State = {
        val wd = state.wd
        if (wd.hasEntry(name)) {
            state.setMsg("Entry " + name + " already exists")
        } else if (name.contains(Directory.SEPARATOR)) {
            state.setMsg(name + " must not contains separators")
        } else if (checkIllegal(name)) {
            state.setMsg(name + ": illegal entry name")
        } else {
            doCreateEntry(state, name)
        }
    }

    def checkIllegal(name: String): Boolean = {
        name.contains(".")
    }

    def doCreateEntry(state: State, name: String): State = {

        def updateStructure(currentDir: Directory, path: List[String], newEntry: DirEntry): Directory = {
            if (path.isEmpty) currentDir.addEntry(newEntry)
            else {
                val oldEntry = currentDir.findEntry(path.head).asDir
                currentDir.replaceEntry(oldEntry.name, updateStructure(oldEntry, path.tail, newEntry))
            }
        }

        //val newDir = Directory.empty(wd.path, name)
        val allDirsInPath = state.wd.getAllFoldersInPath
        val newEntry: DirEntry = createEntry(state)
        val newRoot = updateStructure(state.root, allDirsInPath, newEntry)
        State(newRoot, newRoot.findDescendant(allDirsInPath))

    }

    def createEntry(state: State): DirEntry
}
