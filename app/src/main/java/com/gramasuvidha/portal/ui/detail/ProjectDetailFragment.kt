package com.gramasuvidha.portal.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import java.util.Locale

class ProjectDetailFragment : Fragment() {

    private var _binding: FragmentProjectDetailBinding? = null
    private val binding get() = _binding!!

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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
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
                        }
                        
                        // Show details
                        binding.layoutSplit.visibility = View.VISIBLE
                        binding.layoutComponents.visibility = View.VISIBLE
                        
                        setMockDetails(it.id, currentLang)
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
