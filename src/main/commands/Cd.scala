package main.commands

import main.files.{DirEntry, Directory}
import main.filesystem.State

import scala.annotation.tailrec

class Cd(dir: String) extends Command {
    override def apply(state: State): State = {
        val root = state.root
        val wd = state.wd
        val absolutePath =
            if (dir.startsWith(Directory.SEPARATOR)) dir
            else if (wd.isRoot) wd.path + dir
            else wd.path + Directory.SEPARATOR + dir

        val destDir = doFindEntry(root, absolutePath)

        if (destDir == null || !destDir.isDir)
            state.setMsg(dir + ": no such directory")
        else
            State(root, destDir.asDir)
    }

    def doFindEntry(root: Directory, path: String): DirEntry = {
        @tailrec
        def findEntryHelper(dir: Directory, path: List[String]): DirEntry = {
            if (path.isEmpty || path.head.isEmpty) dir
            else if (path.tail.isEmpty) dir.findEntry(path.head)
            else {
                val nextDir = dir.findEntry(path.head)
                if (nextDir == null || !nextDir.isDir) null
                else findEntryHelper(nextDir.asDir, path.tail)
            }
        }

        @tailrec
        def collapseRelativeTokens(path: List[String], res: List[String]): List[String] = {
            if (path.isEmpty) res
            else if (".".equals(path.head)) collapseRelativeTokens(path.tail, res)
            else if ("..".equals(path.head)) {
                if (res.isEmpty) null
                else collapseRelativeTokens(path.tail, res.tail)
            } else collapseRelativeTokens(path.tail, res :+ path.head)
        }

        val tokens: List[String] = collapseRelativeTokens(
            path
              .substring(1)
              .split(Directory.SEPARATOR)
              .toList,
            List())

        findEntryHelper(root, tokens)
    }
}
