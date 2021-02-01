package main.commands

import main.files.Directory
import main.filesystem.State

class Rm(name: String) extends Command {
    override def apply(state: State): State = {
        val wd = state.wd
        val absolutePath =
            if (name.startsWith(Directory.SEPARATOR)) name
            else if (wd.isRoot) wd.path + name
            else wd.path + Directory.SEPARATOR + name

        if (Directory.ROOT_PATH.equals(absolutePath))
            state.setMsg("not supported")
        else doRm(state, absolutePath)
    }

    def doRm(state: State, path: String): State = {

        def rmHelper(currentDir: Directory, path: List[String]): Directory = {
            if (path.isEmpty) currentDir
            else if (path.tail.isEmpty) currentDir.removeEntry(path.head)
            else {
                val nextDir = currentDir.findEntry(path.head)
                if (!nextDir.isDir) currentDir
                else {
                    val newNextDir = rmHelper(nextDir.asDir, path.tail)
                    if (newNextDir == nextDir) currentDir
                    else currentDir.replaceEntry(path.head, newNextDir)
                }
            }
        }

        val newRoot: Directory = rmHelper(state.root, path.substring(1).split(Directory.SEPARATOR).toList)

        if (newRoot == state.root)
            state.setMsg(path + ": no such file or directory")
        else
            State(newRoot, newRoot.findDescendant(state.wd.path.substring(1)))
    }

}
