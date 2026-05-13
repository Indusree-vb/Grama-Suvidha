package com.gramasuvidha.portal.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.gramasuvidha.portal.data.local.entities.ProjectEntity
import com.gramasuvidha.portal.databinding.DialogAddProjectBinding
import java.util.*

class AddProjectDialog(private val onProjectAdded: (ProjectEntity) -> Unit) : DialogFragment() {

    private var _binding: DialogAddProjectBinding? = null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddProjectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSave.setOnClickListener {
            val nameEn = binding.etProjectNameEn.text.toString()
            val nameKn = binding.etProjectNameKn.text.toString()
            val location = binding.etLocation.text.toString()
            val budget = binding.etBudget.text.toString()

            if (nameEn.isBlank() || nameKn.isBlank()) {
                Toast.makeText(context, "Please enter project names", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newProject = ProjectEntity(
                id = UUID.randomUUID().toString(),
                nameEn = nameEn,
                nameKn = nameKn,
                locationEn = location,
                locationKn = location,
                budget = budget,
                expectedCompletionDate = "2025-12-31",
                statusEn = "PLANNED",
                statusKn = "ಯೋಜಿಸಲಾಗಿದೆ",
                completionPercentage = 0,
                descriptionEn = "New project added by admin",
                descriptionKn = "ನಿರ್ವಾಹಕರು ಹೊಸ ಯೋಜನೆಯನ್ನು ಸೇರಿಸಿದ್ದಾರೆ",
                beforeImageUrl = "https://tender.constrofacilitator.com/wp-content/uploads/2023/07/building-construction.jpg",
                afterImageUrl = ""
            )

            onProjectAdded(newProject)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "AddProjectDialog"
    }
}
