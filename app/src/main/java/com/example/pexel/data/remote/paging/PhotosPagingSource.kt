// data/remote/paging/PhotosPagingSource.kt
package com.example.pexel.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pexel.data.remote.Api.ApiServices
import com.example.pexel.data.remote.models.photo.PexelsPhoto
import retrofit2.HttpException
import java.io.IOException

class PhotosPagingSource(
    private val apiService: ApiServices,
    private val query: String? = null
) : PagingSource<Int, PexelsPhoto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PexelsPhoto> {
        return try {
            val page = params.key ?: 1
            val perPage = params.loadSize

            val response = if (query.isNullOrEmpty()) {
                apiService.getPopularImages(page = page, perPage = perPage)
            } else {
                apiService.searchPhotos(query = query, page = page, perPage = perPage)
            }

            LoadResult.Page(
                data = response.photos,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.photos.isEmpty()) null else page + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PexelsPhoto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}