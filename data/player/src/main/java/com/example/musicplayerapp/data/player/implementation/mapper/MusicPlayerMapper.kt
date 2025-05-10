package com.example.musicplayerapp.data.player.implementation.mapper

import com.example.musicplayerapp.core.data.ext.orEmpty
import com.example.musicplayerapp.data.player.api.model.Album
import com.example.musicplayerapp.data.player.api.model.Artist
import com.example.musicplayerapp.data.player.api.model.TrackList
import com.example.musicplayerapp.data.player.implementation.remote.response.AlbumResponse
import com.example.musicplayerapp.data.player.implementation.remote.response.ArtistResponse
import com.example.musicplayerapp.data.player.implementation.remote.response.TrackListResponse

internal fun TrackListResponse?.toTrackList(): TrackList {
    return TrackList(
        id = this?.id.orEmpty(),
        album = this?.album.toAlbum(),
        artist = this?.artist.toArtist(),
        duration = this?.duration.orEmpty(),
        preview = this?.preview.orEmpty(),
        title = this?.title.orEmpty(),
    )
}

internal fun AlbumResponse?.toAlbum(): Album {
    return Album(
        id = this?.id.orEmpty(),
        title = this?.title.orEmpty(),
        cover = this?.cover.orEmpty(),
    )
}

internal fun ArtistResponse?.toArtist(): Artist {
    return Artist(
        id = this?.id.orEmpty(),
        name = this?.name.orEmpty(),
    )
}
