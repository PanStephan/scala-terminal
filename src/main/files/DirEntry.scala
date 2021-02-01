package main.files

abstract class DirEntry(val parentPath: String, val name: String) {
    def path: String = {
        val sep =
            if (Directory.ROOT_PATH.equals(parentPath)) ""
            else Directory.SEPARATOR

        parentPath + sep + name
    }

    def asDir: Directory
    def asFile: File

    def isDir: Boolean
    def isFile: Boolean

    def getType: String
}