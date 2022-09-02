package com.sorrowblue.comicviewer.viewer

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.core.graphics.ColorUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.size.Size
import coil.transform.RoundedCornersTransformation
import coil.transform.Transformation
import com.sorrowblue.comicviewer.viewer.databinding.ViewerFragmentComicPageBinding
import com.sorrowblue.jetpack.binding.viewBinding
import kotlinx.coroutines.launch
import logcat.logcat

internal class ComicPageFragment : Fragment(R.layout.viewer_fragment_comic_page) {

    private val binding: ViewerFragmentComicPageBinding by viewBinding()
    private val viewModel: ViewerViewModel by viewModels(::requireParentFragment)
    private val args: ComicPageFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            binding.loading.isVisible = true
            binding.page.load(viewModel.loadPage(args.position).preview, builder = {
                transformations(object : Transformation {
                    override val cacheKey: String
                        get() = javaClass.name

                    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
                        return input.trimBorders(Color.WHITE)
                    }
                })
            })
            binding.loading.isVisible = false
        }
        binding.start.setOnClickListener {
            viewModel.back()
        }
        binding.center.setOnClickListener {
            viewModel.isVisibleUI.value = !viewModel.isVisibleUI.value
        }
        binding.end.setOnClickListener {
            viewModel.next()
        }
    }
    fun Bitmap.trimBorders(color: Int): Bitmap {
        var startX = 0
        loop@ for (x in 0 until width) {
            for (y in 0 until height) {
                if (getPixel(x, y) != color) {
                    startX = x
                    break@loop
                }
            }
        }
        var startY = 0
        loop@ for (y in 0 until height) {
            for (x in 0 until width) {
                if (getPixel(x, y) != color) {
                    startY = y
                    break@loop
                }
            }
        }
        var endX = width - 1
        loop@ for (x in endX downTo 0) {
            for (y in 0 until height) {
                if (getPixel(x, y) != color) {
                    endX = x
                    break@loop
                }
            }
        }
        var endY = height - 1
        loop@ for (y in endY downTo 0) {
            for (x in 0 until width) {
                if (getPixel(x, y) != color) {
                    endY = y
                    break@loop
                }
            }
        }

        val newWidth = endX - startX + 1
        val newHeight = endY - startY + 1

        return Bitmap.createBitmap(this, startX, startY, newWidth, newHeight)
    }
}
