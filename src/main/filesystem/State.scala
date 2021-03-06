package main.filesystem

import main.files.Directory

class State(val root: Directory, val wd: Directory, val output: String) {
    def show: Unit = {
        println(output)
        print(State.SHELL_TOKEN)
    }

    def setMsg(msg: String): State =
        State(root, wd, msg)
}

object State {
    val SHELL_TOKEN = "$ "

    def apply(root: Directory, wd: Directory, output: String = ""): State =
        new State(root, wd, output)
}
