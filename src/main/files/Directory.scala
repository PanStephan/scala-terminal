package main.files

import scala.annotation.tailrec

class Directory(override val parentPath: String, override val name: String, val contents: List[DirEntry])
  extends DirEntry(parentPath, name) {

    def hasEntry(name: String): Boolean =
        findEntry(name) != null

    def getAllFoldersInPath: List[String] =
        path.substring(1).split(Directory.SEPARATOR).toList.filter(v => !v.isEmpty)

    def findDescendant(path: List[String]): Directory =
        if (path.isEmpty) this
        else findEntry(path.head).asDir.findDescendant(path.tail)

    def findDescendant(relPath: String): Directory =
        if (relPath.isEmpty) this
        else findDescendant(relPath.split(Directory.SEPARATOR).toList)

    def removeEntry(name: String): Directory = {
        if (!hasEntry(name)) this
        else new Directory(parentPath, name, contents.filter(x => !x.name.equals(name)))
    }

    def addEntry(newEntry: DirEntry): Directory =
        new Directory(parentPath, name, contents :+ newEntry)

    def findEntry(entryName: String): DirEntry = {
        @tailrec
        def findEntryHelper(name: String, contentList: List[DirEntry]): Directory = {
            if (contentList.isEmpty) null
            else if (contentList.head.name.equals(name)) contentList.head.asDir
            else findEntryHelper(name, contentList.tail)
        }
        findEntryHelper(entryName, contents)
    }


    def replaceEntry(entryName: String, newEntry: DirEntry): Directory =
        new Directory(parentPath, name, contents.filter(e => !e.name.equals(entryName)) :+ newEntry)

    def isDir: Boolean = true
    def isFile: Boolean = false

    def isRoot: Boolean = parentPath.isEmpty
    def asDir = this
    def asFile: File =
        throw new FileSystemException("a dir cannot converted to a file")
    def getType: String = "Directory"
}

object Directory {
    val SEPARATOR = "/"
    val ROOT_PATH = "/"

    def ROOT = Directory.empty("", "")

    def empty(parentPath: String, name: String): Directory =
        new Directory(parentPath, name, List())
}
