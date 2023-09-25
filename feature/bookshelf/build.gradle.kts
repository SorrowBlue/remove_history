plugins {
    id("comicviewer.android.feature")
}

android {
    namespace = "com.sorrowblue.comicviewer.feature.bookshelf"
    resourcePrefix("bookshelf")
}

dependencies {
    implementation(projects.framework)
    implementation(projects.feature.folder)
    implementation(projects.feature.bookshelf.selection)
    implementation(projects.feature.bookshelf.edit)
}
