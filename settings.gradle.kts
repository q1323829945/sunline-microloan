rootProject.name = "microloan"

File("modules").walk().filter { it.isDirectory && it.name.contains("lib-") }.forEach {
    dir ->
    include(dir.name)
    project(":${dir.name}").projectDir = dir
}

File("apps").walk().filter { it.isDirectory && it.name.startsWith("app-") }.forEach {
    dir ->
    include(dir.name)
    project(":${dir.name}").projectDir = dir
}

File("tests").walk().filter { it.isDirectory && it.name.startsWith("test-") }.forEach {
    dir ->
    include(dir.name)
    project(":${dir.name}").projectDir = dir
}
