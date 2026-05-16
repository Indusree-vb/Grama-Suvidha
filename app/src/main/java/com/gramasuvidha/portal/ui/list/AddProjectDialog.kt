package com.gramasuvidha.portal.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import com.gramasuvidha.portal.data.local.entities.ProjectEntity
import com.gramasuvidha.portal.databinding.DialogAddProjectBinding
import java.io.File
import java.util.*

class AddProjectDialog(private val onProjectAdded: (ProjectEntity) -> Unit) : DialogFragment() {

    private var _binding: DialogAddProjectBinding? = null
    private val binding get() = _binding!!

    private var beforeImageUri: String = ""
    private var afterImageUri: String = ""

    private val captureBeforeLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            Toast.makeText(context, "Before photo captured", Toast.LENGTH_SHORT).show()
        }
    }

    private val captureAfterLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            Toast.makeText(context, "After photo captured", Toast.LENGTH_SHORT).show()
        }
    }

    private val galleryBeforeLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val savedUri = copyUriToInternalStorage(it)
            beforeImageUri = savedUri.toString()
            Toast.makeText(context, "Before photo selected", Toast.LENGTH_SHORT).show()
        }
    }

    private val galleryAfterLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val savedUri = copyUriToInternalStorage(it)
            afterImageUri = savedUri.toString()
            Toast.makeText(context, "After photo selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun copyUriToInternalStorage(uri: android.net.Uri): android.net.Uri {
        val storageDir = File(requireContext().filesDir, "project_images")
        if (!storageDir.exists()) storageDir.mkdirs()
        
        val destinationFile = File(storageDir, "GAL_${System.currentTimeMillis()}.jpg")
        
        try {
            requireContext().contentResolver.openInputStream(uri)?.use { input ->
                destinationFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.provider", destinationFile)
    }

    private fun getTmpFileUri(isBefore: Boolean): android.net.Uri {
        val storageDir = File(requireContext().filesDir, "project_images")
        if (!storageDir.exists()) storageDir.mkdirs()
        
        val photoFile = File(storageDir, "IMG_${System.currentTimeMillis()}.jpg").apply {
            createNewFile()
        }
        val uri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.provider", photoFile)
        if (isBefore) beforeImageUri = uri.toString() else afterImageUri = uri.toString()
        return uri
    }

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

        binding.btnCaptureBefore.setOnClickListener {
            captureBeforeLauncher.launch(getTmpFileUri(true))
        }

        binding.btnGalleryBefore.setOnClickListener {
            galleryBeforeLauncher.launch("image/*")
        }

        binding.btnCaptureAfter.setOnClickListener {
            captureAfterLauncher.launch(getTmpFileUri(false))
        }

        binding.btnGalleryAfter.setOnClickListener {
            galleryAfterLauncher.launch("image/*")
        }

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
                coverImageUrl = if (beforeImageUri.isNotEmpty()) beforeImageUri else "https://plus.unsplash.com/premium_photo-1682144365727-46387532a84b?auto=format&fit=crop&w=800&q=80",
                beforeImageUrl = beforeImageUri,
                afterImageUrl = afterImageUri
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
