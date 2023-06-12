package ru.ilya.moviereviews.data.mapper

import ru.ilya.moviereviews.data.local.entity.CriticEntity
import ru.ilya.moviereviews.data.local.entity.ReviewEntity
import ru.ilya.moviereviews.data.remote.dto.critics_dto.CriticDto
import ru.ilya.moviereviews.data.remote.dto.reviews_dto.ReviewDto
import ru.ilya.moviereviews.domain.model.critic_model.Critic
import ru.ilya.moviereviews.domain.model.review_model.Review
import javax.inject.Inject

class AppMapper @Inject constructor() {

    fun mapReviewEntityToReview(reviewEntity: ReviewEntity): Review {
        return Review(
            byline = reviewEntity.byline,
            displayTitle = reviewEntity.displayTitle,
            reviewUrl = reviewEntity.reviewUrl,
            pictureUrl = reviewEntity.pictureUrl,
            publicationDate = reviewEntity.publicationDate,
            summaryShort = reviewEntity.summaryShort
        )
    }

    fun mapListCriticDtoToListCriticEntity(criticsDto: List<CriticDto>): List<CriticEntity> {
        return criticsDto.map { mapCriticDtoToCriticEntity(it) }
    }

    private fun mapCriticDtoToCriticEntity(criticDto: CriticDto): CriticEntity {
        return CriticEntity(
            bio = criticDto.bio ?: EMPTY_LINE,
            displayName = criticDto.displayName ?: EMPTY_LINE,
            status = criticDto.status ?: EMPTY_LINE,
            criticPhoto = criticDto.multimedia?.resource?.src ?: EMPTY_LINE
        )
    }

    fun mapListCriticEntityToListCritic(criticsEntity: List<CriticEntity>): List<Critic> {
        return criticsEntity.map { mapCriticEntityToCritic(it) }
    }

    private fun mapCriticEntityToCritic(criticEntity: CriticEntity): Critic {
        return Critic(
            bio = criticEntity.bio,
            displayName = criticEntity.displayName,
            status = criticEntity.status,
            photoUrl = criticEntity.criticPhoto
        )
    }

    fun mapListReviewDtoToListReviewEntity(reviewsDto: List<ReviewDto>): List<ReviewEntity> {
        return reviewsDto.map { mapReviewDtoToReviewEntity(it) }
    }

    private fun mapReviewDtoToReviewEntity(reviewDto: ReviewDto): ReviewEntity {
        return ReviewEntity(
            byline = reviewDto.byline ?: EMPTY_LINE,
            displayTitle = reviewDto.displayTitle ?: EMPTY_LINE,
            reviewUrl = reviewDto.link.url ?: EMPTY_LINE,
            pictureUrl = reviewDto.multimedia?.src ?: EMPTY_LINE,
            publicationDate = reviewDto.dateUpdated ?: EMPTY_LINE,
            summaryShort = reviewDto.summaryShort ?: EMPTY_LINE
        )
    }

    companion object {

        const val EMPTY_LINE = ""
    }
}