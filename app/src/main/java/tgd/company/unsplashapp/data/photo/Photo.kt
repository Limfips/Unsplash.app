package tgd.company.unsplashapp.data.photo

data class Photo(
    val id: String,
    val urls: Urls,
    val width: Int,
    val height: Int,
    val current_user_collections: List<CurrentUserCollection>
)