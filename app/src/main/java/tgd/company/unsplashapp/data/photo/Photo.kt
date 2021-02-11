package tgd.company.unsplashapp.data.photo

data class Photo(
    val id: String,
    val urls: Urls,
    val width: Int,
    val height: Int,
    val description: String
)