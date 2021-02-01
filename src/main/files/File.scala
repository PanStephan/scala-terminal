package main.files

class File(override val parentPath: String, override val name: String, val content: String)
  extends DirEntry(parentPath, name) {

    def asDir: Directory =
        throw new FileSystemException("a file cannot be converted to dir")

    def asFile: File = this

    def isDir: Boolean = false
    def isFile: Boolean = true

    def setContents(cont: String): File =
        new File(parentPath, name, cont)


    def appendContents(cont: String): File =
        setContents(content + "\n" + cont)

    def getType: String = "File"
}

object File {
    def empty(parentPath: String, name: String): File =
        new File(parentPath, name, "")
}
