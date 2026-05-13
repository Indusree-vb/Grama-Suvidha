package com.gramasuvidha.portal.ui.feedback

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.gramasuvidha.portal.R
import com.gramasuvidha.portal.data.local.AppDatabase
import com.gramasuvidha.portal.data.local.entities.Feedback
import com.gramasuvidha.portal.data.repository.ProjectRepository
import com.gramasuvidha.portal.databinding.DialogFeedbackBinding
import kotlinx.coroutines.launch

class FeedbackDialog : DialogFragment() {

    private var _binding: DialogFeedbackBinding? = null
    private val binding get() = _binding!!

    private var projectId: String = ""
    private var isIssueInitial: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        projectId = arguments?.getString(ARG_PROJECT_ID) ?: ""
        isIssueInitial = arguments?.getBoolean(ARG_IS_ISSUE) ?: false
        
        // Use standard Dialog theme for better interactivity of custom layouts
        setStyle(STYLE_NORMAL, com.google.android.material.R.style.Theme_MaterialComponents_Light_Dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFeedbackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = AppDatabase.getDatabase(requireContext())
        val repository = ProjectRepository(database.projectDao(), database.feedbackDao())

        // Explicitly ensure RatingBar is interactive
        // Using setIsIndicator(false) because isIndicator can be treated as a read-only property in some contexts
        binding.ratingBar.setIsIndicator(false)
        binding.ratingBar.isEnabled = true
        binding.ratingBar.rating = 3f

        // Set initial state
        binding.switchIsIssue.isChecked = isIssueInitial
        updateUiForMode(isIssueInitial)

        // Change hint based on whether it's an issue or general feedback
        binding.switchIsIssue.setOnCheckedChangeListener { _, isChecked ->
            updateUiForMode(isChecked)
        }

        binding.btnSubmit.setOnClickListener {
            val name = binding.etUserName.text.toString().trim().ifBlank { "Anonymous" }
            val description = binding.etComment.text.toString().trim()
            val rating = binding.ratingBar.rating.toInt()
            val isIssue = binding.switchIsIssue.isChecked

            if (description.isNotBlank()) {
                val feedback = Feedback(
                    projectId = projectId,
                    rating = rating,
                    issueDescription = description,
                    isIssue = isIssue,
                    citizenName = name
                )
                lifecycleScope.launch {
                    repository.insertFeedback(feedback)
                    Toast.makeText(context, R.string.feedback_submitted, Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            } else {
                Toast.makeText(context, R.string.error_empty_feedback, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUiForMode(isIssue: Boolean) {
        if (isIssue) {
            binding.etComment.setHint(R.string.issue_description)
            binding.dialogTitle.setText(R.string.report_issue)
        } else {
            binding.etComment.setHint(R.string.your_feedback)
            binding.dialogTitle.setText(R.string.submit_feedback)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_PROJECT_ID = "projectId"
        private const val ARG_IS_ISSUE = "isIssue"

        fun newInstance(projectId: String, isIssue: Boolean = false): FeedbackDialog {
            val fragment = FeedbackDialog()
            val args = Bundle()
            args.putString(ARG_PROJECT_ID, projectId)
            args.putBoolean(ARG_IS_ISSUE, isIssue)
            fragment.arguments = args
            return fragment
        }
    }
}
