// data/remote/paging/VideosPagingSource.kt
package com.example.pexel.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pexel.data.remote.Api.ApiServices
import com.example.pexel.data.remote.models.video.PexelVideo
import retrofit2.HttpException
import java.io.IOException

class VideosPagingSource(
    private val apiService: ApiServices,
    private val query: String? = null
) : PagingSource<Int, PexelVideo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PexelVideo> {
        return try {
            val page = params.key ?: 1
            val perPage = params.loadSize

            val response = if (query.isNullOrEmpty()) {
                apiService.getPopularVideos(page = page, perPage = perPage)
            } else {
                apiService.searchVideos(query = query, page = page, perPage = perPage)
            }

            LoadResult.Page(
                data = response.videos,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.videos.isEmpty()) null else page + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PexelVideo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}