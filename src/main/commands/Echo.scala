package main.commands

import main.files.{Directory, File}
import main.filesystem.State

class Echo(args: Array[String]) extends Command {
    override def apply(state: State): State = {
        if (args.length == 0) state
        else if (args.length == 1) state.setMsg(args(0))
        else {
            val operator = args(args.length - 2)
            val filename = args(args.length - 1)
            val cont = createContent(args, args.length - 2)

            if (">>".equals(operator))
                doEcho(state, cont, filename, true)
            else if (">".equals(operator))
                doEcho(state, cont, filename)
            else state.setMsg(createContent(args, args.length))
        }
    }

    def getRootAfterEcho(currDir: Directory, path: List[String], cont: String, append: Boolean = false) : Directory= {
        if (path.isEmpty) currDir
        else if (path.tail.isEmpty) {
            val dirEntry = currDir.findEntry(path.head)
            if (dirEntry == null)
                currDir.addEntry(new File(currDir.path, path.head, cont))
            else if (dirEntry.isDir) currDir
            else
                if (append) currDir.replaceEntry(path.head, dirEntry.asFile.setContents(cont))
                else currDir.replaceEntry(path.head, dirEntry.asFile.appendContents(cont))
        }
        else {
            val nextDir = currDir.findEntry(path.head).asDir
            val newNextDir = getRootAfterEcho(nextDir, path.tail, cont, append)

            if (newNextDir == nextDir) currDir
            else currDir.replaceEntry(path.head, newNextDir)
        }
    }

    def doEcho(state: State, cont: String, filename: String, append: Boolean = false): State = {
        if (filename.contains(Directory.SEPARATOR))
            state.setMsg("not cont sep")
        else {
            val newRoot: Directory = getRootAfterEcho(state.root,state.wd.getAllFoldersInPath :+ filename, cont, append)
            if (newRoot == state.root)
                state.setMsg(filename + " : no such folder")
            else {
                State(newRoot, newRoot.findDescendant(state.wd.getAllFoldersInPath))
            }
        }
    }

    def createContent(args: Array[String], topI: Int): String = {
        def createContentHelper(currI: Int, accumulator: String): String = {
            if (currI >= topI) accumulator
            else createContentHelper(currI + 1, accumulator + " " + args(currI))
        }
        createContentHelper(0, "")
    }



}
