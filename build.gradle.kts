plugins {
    alias(libs.plugins.agp.app) apply false
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.kotlin.compose.compiler) apply false
}

project.ext.set("kernelPatchVersion", "0.13.3")

val androidMinSdkVersion by extra(26)
val androidTargetSdkVersion by extra(36)
val androidCompileSdkVersion by extra(36)
val androidBuildToolsVersion by extra("36.1.0")
val androidCompileNdkVersion by extra("29.0.14206865")
// 手动指定版本号（从官方获取）
val OFFICIAL_VERSION_CODE = 20743  // 官方当前的 VERSION_CODE
val MY_OFFSET = 1  // 你的自定义偏移量，每次发布自增1

val managerVersionCode by extra(OFFICIAL_VERSION_CODE + MY_OFFSET)
// 版本名可以继续使用 Git Hash，或者手动指定
val managerVersionName by extra(getVersionName())
val branchName by extra(getBranch())
fun Project.exec(command: String) = providers.exec {
    commandLine(command.split(" "))
}.standardOutput.asText.get().trim()

fun getGitCommitCount(): Int {
    return exec("git rev-list --count HEAD").trim().toInt()
}

fun getGitDescribe(): String {
    return exec("git rev-parse --verify --short HEAD").trim()
}

fun getVersionCode(): Int {
    val commitCount = getGitCommitCount()
    val major = 1
    return major * 10000 + commitCount + 200
}

fun getBranch(): String {
    return exec("git rev-parse --abbrev-ref HEAD").trim()
}

fun getVersionName(): String {
    return getGitDescribe()
}

tasks.register("printVersion") {
    doLast {
        println("Version code: $managerVersionCode")
        println("Version name: $managerVersionName")
    }
}
