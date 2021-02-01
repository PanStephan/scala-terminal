package main.filesystem

import java.util.Scanner

import main.commands.Command
import main.files.Directory

object FileSystem extends App {

    val root = Directory.ROOT
    var state = State(root, root)
    val Scanner = new Scanner(System.in)

    while (true) {
        state.show
        val input = Scanner.nextLine()
        state = Command.from(input).apply(state)
    }
}
