package ru.ilya.moviereviews.data.mapper

import ru.ilya.moviereviews.data.local.entity.ReviewEntity
import ru.ilya.moviereviews.data.remote.dto.LinkDto
import ru.ilya.moviereviews.data.remote.dto.MultimediaDto
import ru.ilya.moviereviews.data.remote.dto.ReviewDto
import ru.ilya.moviereviews.domain.model.Link
import ru.ilya.moviereviews.domain.model.Multimedia
import ru.ilya.moviereviews.domain.model.Review

class AppMapper {

    fun mapReviewToReviewEntity(review: Review): ReviewEntity {
        return ReviewEntity(
            byline = review.byline,
            displayTitle = review.displayTitle,
            reviewUrl = review.reviewUrl,
            pictureUrl = review.pictureUrl,
            publicationDate = review.publicationDate,
            summaryShort = review.summaryShort
        )
    }

    fun mapListReviewEntityToListReview(reviewsEntity: List<ReviewEntity>): List<Review> {
        return reviewsEntity.map { mapReviewEntityToReview(it) }
    }

    private fun mapReviewEntityToReview(reviewEntity: ReviewEntity): Review {
        return Review(
            byline = reviewEntity.byline,
            displayTitle = reviewEntity.displayTitle,
            reviewUrl = reviewEntity.reviewUrl,
            pictureUrl = reviewEntity.pictureUrl,
            publicationDate = reviewEntity.publicationDate,
            summaryShort = reviewEntity.summaryShort
        )
    }

    fun mapListReviewDtoToListReview(reviewsDto: List<ReviewDto>): List<Review> {
        return reviewsDto.map { mapReviewDtoToReview(it) }
    }

    private fun mapReviewDtoToReview(reviewDto: ReviewDto): Review {
        return Review(
            byline = reviewDto.byline ?: EMPTY_LINE,
            displayTitle = reviewDto.displayTitle ?: EMPTY_LINE,
            reviewUrl = reviewDto.link.url?: EMPTY_LINE,
            pictureUrl = reviewDto.multimedia.src?: EMPTY_LINE,
            publicationDate = reviewDto.publicationDate ?: EMPTY_LINE,
            summaryShort = reviewDto.summaryShort ?: EMPTY_LINE
        )
    }

    companion object {

        const val DEFAULT_INT = 0
        const val EMPTY_LINE = ""
    }
}