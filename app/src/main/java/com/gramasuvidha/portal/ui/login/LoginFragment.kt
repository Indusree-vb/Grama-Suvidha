package com.gramasuvidha.portal.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.gramasuvidha.portal.R
import com.gramasuvidha.portal.data.local.AppDatabase
import com.gramasuvidha.portal.data.repository.UserRepository
import com.gramasuvidha.portal.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private var isSettingInitialLang = false

    private val viewModel: LoginViewModel by viewModels {
        val database = AppDatabase.getDatabase(requireContext())
        val repository = UserRepository(database.userDao())
        object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel(repository) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLanguageToggle()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginResult.collect { result ->
                    val (success, role) = result
                    if (success) {
                        val bundle = Bundle().apply {
                            putString("userRole", role)
                        }
                        findNavController().navigate(
                            R.id.action_loginFragment_to_projectListFragment,
                            bundle
                        )
                    } else {
                        val message = if (role == "Role mismatch") "Selected role does not match account" else getString(R.string.invalid_credentials)
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val selectedRole = if (binding.roleToggle.checkedButtonId == R.id.btnPanchayatAdmin) "Admin" else "User"
            viewModel.login(email, password, selectedRole)
        }

        binding.tvCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun setupLanguageToggle() {
        isSettingInitialLang = true
        val appLocales = AppCompatDelegate.getApplicationLocales()
        val currentLang = if (!appLocales.isEmpty) appLocales.get(0)?.language ?: "en" else "en"
            
        if (currentLang.startsWith("kn")) {
            binding.languageToggle.check(R.id.btnKannada)
        } else {
            binding.languageToggle.check(R.id.btnEnglish)
        }
        isSettingInitialLang = false

        binding.languageToggle.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked && !isSettingInitialLang) {
                val lang = if (checkedId == R.id.btnKannada) "kn" else "en"
                val activeLocales = AppCompatDelegate.getApplicationLocales()
                val activeLang = if (!activeLocales.isEmpty) activeLocales.get(0)?.language ?: "en" else "en"

                if (!activeLang.startsWith(lang)) {
                    updateLocale(lang)
                }
            }
        }
    }

    private fun updateLocale(lang: String) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(lang)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
