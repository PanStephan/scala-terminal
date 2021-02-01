package main.commands

import main.filesystem.State

trait Command {
    def apply(state: State): State
}

object Command {
    val MKDIR = "mkdir"
    val LS = "ls"
    val PWD ="pwd"
    val TOUCH = "touch"
    val CD = "cd"
    val RM = "rm"
    val ECHO = "echo"
    val CAT = "cat"

    def emptyCommand = new Command {
        override def apply(state: State): State = state
    }

    def incompleteCommand(name: String) = new Command {
        override def apply(state: State): State =
            state.setMsg(name + ": incomplete command")
    }

    def from(input: String): Command = {
        val tokens: Array[String] = input.split(" ")

        if (input.isEmpty || tokens.isEmpty) emptyCommand
        else if (MKDIR.equals(tokens(0))) {
            if (tokens.length < 2) incompleteCommand("mkdir")
            else new Mkdir(tokens(1))
        }
        else if (TOUCH.equals(tokens(0))) {
            if (tokens.length < 2) incompleteCommand("touch")
            else new Touch(tokens(1))
        }
        else if (CD.equals(tokens(0))) {
            if (tokens.length < 2) incompleteCommand("cd")
            else new Cd(tokens(1))
        }
        else if (RM.equals(tokens(0))) {
            if (tokens.length < 2) incompleteCommand("rm")
            else new Rm(tokens(1))
        }
        else if (ECHO.equals(tokens(0))) {
            if (tokens.length < 2) incompleteCommand("echo")
            else new Echo(tokens.tail)
        }
        else if (CAT.equals(tokens(0))) {
            if (tokens.length < 2) incompleteCommand("cat")
            else new Cat(tokens(1))
        }
        else if (LS.equals(tokens(0))) new Ls
        else if (PWD.equals(tokens(0))) new Pwd
        else new UnknownCmd
    }
}
