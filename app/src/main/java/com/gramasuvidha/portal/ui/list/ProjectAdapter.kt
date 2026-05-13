package com.gramasuvidha.portal.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gramasuvidha.portal.data.local.entities.ProjectEntity
import com.gramasuvidha.portal.databinding.ItemProjectBinding
import java.util.Locale

class ProjectAdapter(
    private val userRole: String?,
    private val onDeleteClick: ((ProjectEntity) -> Unit)? = null,
    private val onItemClick: (ProjectEntity) -> Unit
) : ListAdapter<ProjectEntity, ProjectAdapter.ProjectViewHolder>(ProjectDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val binding = ItemProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProjectViewHolder(binding, userRole, onDeleteClick)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val project = getItem(position)
        holder.bind(project)
        holder.itemView.setOnClickListener { onItemClick(project) }
    }

    class ProjectViewHolder(
        private val binding: ItemProjectBinding,
        private val userRole: String?,
        private val onDeleteClick: ((ProjectEntity) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(project: ProjectEntity) {
            val currentLang = Locale.getDefault().language
            
            // Logic to select text based on language
            val displayName = if (currentLang == "kn") project.nameKn else project.nameEn
            val displayLocation = if (currentLang == "kn") project.locationKn else project.locationEn
            val displayStatus = if (currentLang == "kn") project.statusKn else project.statusEn

            binding.project = project
            binding.projectName.text = displayName
            binding.projectLocation.text = displayLocation
            binding.statusBadge.text = displayStatus
            
            // Show admin controls only for Admin role
            binding.adminControls.visibility = if (userRole == "Admin") android.view.View.VISIBLE else android.view.View.GONE
            
            binding.btnDelete.setOnClickListener {
                onDeleteClick?.invoke(project)
            }

            binding.executePendingBindings()
        }
    }

    class ProjectDiffCallback : DiffUtil.ItemCallback<ProjectEntity>() {
        override fun areItemsTheSame(oldItem: ProjectEntity, newItem: ProjectEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProjectEntity, newItem: ProjectEntity): Boolean {
            return oldItem == newItem
        }
    }
}
