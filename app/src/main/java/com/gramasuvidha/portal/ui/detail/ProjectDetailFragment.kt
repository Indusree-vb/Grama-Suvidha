package com.gramasuvidha.portal.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.gramasuvidha.portal.R
import com.gramasuvidha.portal.data.local.AppDatabase
import com.gramasuvidha.portal.data.repository.ProjectRepository
import com.gramasuvidha.portal.databinding.FragmentProjectDetailBinding
import com.gramasuvidha.portal.ui.feedback.FeedbackDialog
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale

class ProjectDetailFragment : Fragment() {

    private var _binding: FragmentProjectDetailBinding? = null
    private val binding get() = _binding!!

    private var currentTmpUri: android.net.Uri? = null
    private var capturingBefore = true

    private val capturePhotoLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && currentTmpUri != null) {
            if (capturingBefore) {
                viewModel.updateProjectPhotos(currentTmpUri.toString(), null)
            } else {
                viewModel.updateProjectPhotos(null, currentTmpUri.toString())
            }
            Toast.makeText(requireContext(), "Photo updated successfully", Toast.LENGTH_SHORT).show()
        }
    }

    private val galleryPhotoLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val savedUri = copyUriToInternalStorage(it)
            if (capturingBefore) {
                viewModel.updateProjectPhotos(savedUri.toString(), null)
            } else {
                viewModel.updateProjectPhotos(null, savedUri.toString())
            }
            Toast.makeText(requireContext(), "Photo updated from gallery", Toast.LENGTH_SHORT).show()
        }
    }

    private fun copyUriToInternalStorage(uri: android.net.Uri): android.net.Uri {
        val storageDir = File(requireContext().filesDir, "project_images")
        if (!storageDir.exists()) storageDir.mkdirs()
        
        val destinationFile = File(storageDir, "GAL_UPD_${System.currentTimeMillis()}.jpg")
        
        try {
            requireContext().contentResolver.openInputStream(uri)?.use { input ->
                destinationFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Failed to copy image", Toast.LENGTH_SHORT).show()
        }
        
        return FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.provider", destinationFile)
    }

    private fun getTmpFileUri(): android.net.Uri {
        val storageDir = File(requireContext().filesDir, "project_images")
        if (!storageDir.exists()) storageDir.mkdirs()
        
        val photoFile = File(storageDir, "IMG_UPD_${System.currentTimeMillis()}.jpg").apply {
            createNewFile()
        }
        return FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.provider", photoFile)
    }

    private val viewModel: ProjectDetailViewModel by viewModels {
        val database = AppDatabase.getDatabase(requireContext())
        val repository = ProjectRepository(database.projectDao(), database.feedbackDao())
        object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return ProjectDetailViewModel(repository) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val projectId = arguments?.getString("projectId") ?: ""
        viewModel.loadProject(projectId)

        val feedbackAdapter = FeedbackAdapter()
        binding.feedbackRecyclerView.adapter = feedbackAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.project.collect { project ->
                        project?.let {
                            val currentLang = Locale.getDefault().language
                            binding.project = it
                            
                            // Localized text
                            binding.detailName.text = if (currentLang == "kn") it.nameKn else it.nameEn
                            binding.detailDescription.text = if (currentLang == "kn") it.descriptionKn else it.descriptionEn
                            
                            // Update UI labels
                            binding.btnFeedback.text = getString(R.string.give_feedback)
                            binding.btnReportIssue.text = getString(R.string.report_issue)
                            binding.titleWorkSplit.text = getString(R.string.work_amount_split)
                            binding.titleComponents.text = getString(R.string.components_used)
                            
                            // Role-based visibility
                            val role = arguments?.getString("userRole")
                            if (role == "Admin") {
                                // Admins see everything + maybe some extra tags
                                binding.titleWorkSplit.text = "${binding.titleWorkSplit.text} (Internal Audit)"
                                binding.adminPhotoControls.visibility = View.VISIBLE
                            }
                            
                            // Show details
                            binding.layoutSplit.visibility = View.VISIBLE
                            binding.layoutComponents.visibility = View.VISIBLE
                            
                            setMockDetails(it.id, currentLang)
                        }
                    }
                }

                launch {
                    viewModel.feedback.collect { feedbackList ->
                        feedbackAdapter.submitList(feedbackList)
                        binding.titleRecentFeedback.visibility = if (feedbackList.isEmpty()) View.GONE else View.VISIBLE
                    }
                }
            }
        }

        binding.btnFeedback.setOnClickListener {
            val dialog = FeedbackDialog.newInstance(projectId, isIssue = false)
            dialog.show(childFragmentManager, "FeedbackDialog")
        }

        binding.btnReportIssue.setOnClickListener {
            val dialog = FeedbackDialog.newInstance(projectId, isIssue = true)
            dialog.show(childFragmentManager, "FeedbackDialog")
        }

        binding.tvQuickFeedback.setOnClickListener {
            val dialog = FeedbackDialog.newInstance(projectId, isIssue = false)
            dialog.show(childFragmentManager, "FeedbackDialog")
        }

        binding.btnAdminCaptureBefore.setOnClickListener {
            capturingBefore = true
            currentTmpUri = getTmpFileUri()
            capturePhotoLauncher.launch(currentTmpUri!!)
        }

        binding.btnAdminGalleryBefore.setOnClickListener {
            capturingBefore = true
            galleryPhotoLauncher.launch("image/*")
        }

        binding.btnAdminCaptureAfter.setOnClickListener {
            capturingBefore = false
            currentTmpUri = getTmpFileUri()
            capturePhotoLauncher.launch(currentTmpUri!!)
        }

        binding.btnAdminGalleryAfter.setOnClickListener {
            capturingBefore = false
            galleryPhotoLauncher.launch("image/*")
        }
    }

    private fun setMockDetails(id: String, lang: String) {
        val isKn = lang == "kn"
        
        when {
            id == "1" || id == "PROJ-001" || id.contains("Road", ignoreCase = true) -> {
                binding.workSplitContent.text = if (isKn) 
                    "ಡಾಂಬರು: ₹6,00,000\nಕೂಲಿ: ₹3,00,000\nಇತರೆ: ₹1,00,000" 
                    else "Bitumen: ₹6,00,000\nLabor: ₹3,00,000\nOther: ₹1,00,000"
                binding.componentsContent.text = if (isKn)
                    "1. ಡಾಂಬರು (VG-30)\n2. ಜಲ್ಲಿ ಕಲ್ಲುಗಳು\n3. ರೋಲರ್ ಯಂತ್ರ"
                    else "1. Bitumen (VG-30)\n2. Crushed Stones\n3. Road Roller"
            }
            id == "2" || id == "PROJ-013" || id.contains("Water", ignoreCase = true) || id.contains("Borewell", ignoreCase = true) -> {
                binding.workSplitContent.text = if (isKn)
                    "ಪೈಪ್‌ಗಳು: ₹2,00,000\nಪಂಪ್ ಸೆಟ್: ₹1,00,000\nಕೂಲಿ: ₹50,000"
                    else "Pipes: ₹2,00,000\nPump Set: ₹1,00,000\nLabor: ₹50,000"
                binding.componentsContent.text = if (isKn)
                    "1. PVC ಪೈಪ್‌ಗಳು\n2. ಸಬ್ಮರ್ಸಿಬಲ್ ಪಂಪ್\n3. ಸೋಲಾರ್ ಪ್ಯಾನಲ್"
                    else "1. PVC Pipes\n2. Submersible Pump\n3. Solar Panels"
            }
            id == "3" || id == "PROJ-005" || id == "PROJ-010" || id.contains("Hall", ignoreCase = true) || id.contains("Temple", ignoreCase = true) || id.contains("Library", ignoreCase = true) -> {
                binding.workSplitContent.text = if (isKn)
                    "ಸಿಮೆಂಟ್: ₹4,00,000\nಇಟ್ಟಿಗೆಗಳು: ₹2,00,000\nಬಣ್ಣ: ₹1,00,000\nಕೂಲಿ: ₹1,00,000"
                    else "Cement: ₹4,00,000\nBricks: ₹2,00,000\nPaint: ₹1,00,000\nLabor: ₹1,00,000"
                binding.componentsContent.text = if (isKn)
                    "1. ಪೋರ್ಟ್ಲ್ಯಾಂಡ್ ಸಿಮೆಂಟ್\n2. ಕೆಂಪು ಇಟ್ಟಿಗೆಗಳು\n3. ಟೈಲ್ಸ್\n4. ಎಲ್ಇಡಿ ದೀಪಗಳು"
                    else "1. Portland Cement\n2. Red Bricks\n3. Floor Tiles\n4. LED Lights"
            }
            id == "PROJ-003" || id.contains("Tree", ignoreCase = true) -> {
                binding.workSplitContent.text = if (isKn)
                    "ಸಸಿಗಳು: ₹1,00,000\nಗೊಬ್ಬರ: ₹50,000\nಬೇಲಿ: ₹50,000\nಕೂಲಿ: ₹50,000"
                    else "Saplings: ₹1,00,000\nFertilizer: ₹50,000\nFencing: ₹50,000\nLabor: ₹50,000"
                binding.componentsContent.text = if (isKn)
                    "1. ಹಣ್ಣಿನ ಸಸಿಗಳು\n2. ಸಾವಯವ ಗೊಬ್ಬರ\n3. ಕಬ್ಬಿಣದ ಬೇಲಿ"
                    else "1. Fruit Saplings\n2. Organic Fertilizer\n3. Iron Fencing"
            }
            else -> {
                binding.workSplitContent.text = if (isKn)
                    "ಸಾಮಗ್ರಿಗಳು: 60%\nಕೂಲಿ: 30%\nನಿರ್ವಹಣೆ: 10%"
                    else "Materials: 60%\nLabor: 30%\nManagement: 10%"
                binding.componentsContent.text = if (isKn)
                    "1. ಗುಣಮಟ್ಟದ ಕಚ್ಚಾ ವಸ್ತುಗಳು\n2. ಪರಿಣಿತ ಕೆಲಸಗಾರರು\n3. ಆಧುನಿಕ ಉಪಕರಣಗಳು"
                    else "1. Quality Raw Materials\n2. Skilled Labor\n3. Modern Equipment"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
