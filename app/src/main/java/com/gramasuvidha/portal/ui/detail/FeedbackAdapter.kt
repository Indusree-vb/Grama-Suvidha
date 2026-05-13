package com.gramasuvidha.portal.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gramasuvidha.portal.R
import com.gramasuvidha.portal.data.local.entities.Feedback

class FeedbackAdapter : ListAdapter<Feedback, FeedbackAdapter.FeedbackViewHolder>(FeedbackDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feedback, parent, false)
        return FeedbackViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedbackViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FeedbackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvCitizenName)
        private val tvComment: TextView = itemView.findViewById(R.id.tvFeedbackComment)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.itemRatingBar)
        private val tvIssueBadge: TextView = itemView.findViewById(R.id.tvIssueBadge)

        fun bind(feedback: Feedback) {
            tvName.text = feedback.citizenName
            tvComment.text = feedback.issueDescription
            ratingBar.rating = feedback.rating.toFloat()
            tvIssueBadge.visibility = if (feedback.isIssue) View.VISIBLE else View.GONE
        }
    }

    class FeedbackDiffCallback : DiffUtil.ItemCallback<Feedback>() {
        override fun areItemsTheSame(oldItem: Feedback, newItem: Feedback): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Feedback, newItem: Feedback): Boolean = oldItem == newItem
    }
}
