package com.gramasuvidha.portal

import android.app.Application
import com.gramasuvidha.portal.data.local.AppDatabase
import com.gramasuvidha.portal.data.local.entities.ProjectEntity
import com.gramasuvidha.portal.data.repository.ProjectRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class GramaApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        
        val database = AppDatabase.getDatabase(this)
        val repository = ProjectRepository(database.projectDao(), database.feedbackDao())
        
        applicationScope.launch {
            // Force refresh database content with fixed covers and empty progress slots
            repository.deleteAllProjects()

            val pkg = packageName
            val allProjects = listOf(
                ProjectEntity(
                    "ID_ROAD", "Main Road Repair - Ward 4", "ಮುಖ್ಯ ರಸ್ತೆ ದುರಸ್ತಿ - ವಾರ್ಡ್ 4", 
                    "Maduvaluru Village", "ಮಡುವಲೂರು ಗ್ರಾಮ", "₹10,00,000", "2024-12-31",
                    "ONGOING", "ಪ್ರಗತಿಯಲ್ಲಿದೆ", 85, 
                    "Repair and maintenance of the main connecting road.", 
                    "ಮುಖ್ಯ ಸಂಪರ್ಕ ರಸ್ತೆಯ ದುರಸ್ತಿ ಮತ್ತು ನಿರ್ವಹಣೆ.", 
                    "android.resource://$pkg/drawable/img_road", "", ""
                ),
                ProjectEntity(
                    "ID_BORE", "New Borewell Installation", "ಹೊಸ ಬೋರ್‌ವೆಲ್ ಅಳವಡಿಕೆ", 
                    "Goolyapura Village", "ಗೂಳ್ಯಾಪುರ ಗ್ರಾಮ", "₹3,50,000", "2024-05-15",
                    "COMPLETED", "ಪೂರ್ಣಗೊಂಡಿದೆ", 100, 
                    "Drilling and installation of solar borewell for agricultural and domestic use.", 
                    "ಕೃಷಿ ಮತ್ತು ಗೃಹ ಬಳಕೆಗಾಗಿ ಸೌರ ಬೋರ್‌ವೆಲ್ ಕೊರೆಯುವಿಕೆ ಮತ್ತು ಅಳವಡಿಕೆ.", 
                    "android.resource://$pkg/drawable/img_borewell", "", ""
                ),
                ProjectEntity(
                    "ID_HALL", "Community Hall Renovation", "ಸಮುದಾಯ ಭವನ ನವೀಕರಣ", 
                    "Hosur Village", "ಹೊಸೂರು ಗ್ರಾಮ", "₹8,00,000", "2025-03-20",
                    "PLANNED", "ಯೋಜನೆಯಲ್ಲಿದೆ", 0, 
                    "Renovation of existing community hall structure.", 
                    "ಅಸ್ತಿತ್ವದಲ್ಲಿರುವ ಸಮುದಾಯ ಭವನದ ನವೀಕರಣ.", 
                    "android.resource://$pkg/drawable/img_communityhall", "", ""
                ),
                ProjectEntity(
                    "ID_METER", "Smart Water Metering", "ಸ್ಮಾರ್ಟ್ ನೀರಿನ ಮೀಟರಿಂಗ್",
                    "Mallapura Village", "ಮಲ್ಲಾಪುರ ಗ್ರಾಮ", "₹15,20,000", "2024-08-25",
                    "COMPLETED", "ಪೂರ್ಣಗೊಂಡಿದೆ", 100,
                    "Installation of 500 smart meters to prevent water wastage.",
                    "ನೀರು ಪೋಲಾಗುವುದನ್ನು ತಡೆಯಲು 500 ಸ್ಮಾರ್ಟ್ ಮೀಟರ್‌ಗಳ ಅಳವಡಿಕೆ.",
                    "android.resource://$pkg/drawable/img_smartwater", "", ""
                ),
                ProjectEntity(
                    "ID_WASTE", "Waste Management System", "ತ್ಯಾಜ್ಯ ನಿರ್ವಹಣಾ ವ್ಯವಸ್ಥೆ",
                    "Vijayapura Village", "ವಿಜಯಪುರ ಗ್ರಾಮ", "₹12,00,000", "2024-04-10",
                    "COMPLETED", "ಪೂರ್ಣಗೊಂಡಿದೆ", 100,
                    "Implementation of door-to-door waste collection and segregation unit.",
                    "ಮನೆ ಮನೆಗೆ ತೆರಳಿ ಕಸ ಸಂಗ್ರಹಣೆ ಮತ್ತು ವಿಂಗಡಣಾ ಘಟಕದ ಅನುಷ್ಠಾನ.",
                    "android.resource://$pkg/drawable/img_waste", "", ""
                ),
                ProjectEntity(
                    "ID_LIGHT", "Street Light Maintenance", "ಬೀದಿ ದೀಪಗಳ ನಿರ್ವಹಣೆ",
                    "All Streets", "ಎಲ್ಲಾ ಬೀದಿಗಳು", "₹1,80,000", "2024-12-20",
                    "ONGOING", "ನಡೆಯುತ್ತಿದೆ", 90,
                    "Installation and regular maintenance of LED street lights.",
                    "ಎಲ್ ಇ ಡಿ ಬೀದಿ ದೀಪಗಳ ಅಳವಡಿಕೆ ಮತ್ತು ನಿಯಮಿತ ನಿರ್ವಹಣೆ.",
                    "android.resource://$pkg/drawable/img_streetlight", "", ""
                ),
                ProjectEntity(
                    "ID_DRAIN", "Drain Cleaning and Repair", "ಚರಂಡಿ ಸ್ವಚ್ಛಗೊಳಿಸುವಿಕೆ ಮತ್ತು ದುರಸ್ತಿ",
                    "Residential Area", "ವಸತಿ ಪ್ರದೇಶ", "₹3,50,000", "2024-11-15",
                    "ONGOING", "ಪ್ರಗತಿಯಲ್ಲಿದೆ", 30,
                    "Cleaning of clogged drains and minor repairs to ensure proper water flow.",
                    "ಸರಿಯಾದ ನೀರಿನ ಹರಿವನ್ನು ಖಚಿತಪಡಿಸಿಕೊಳ್ಳಲು ಕಟ್ಟಿಕೊಂಡಿರುವ ಚರಂಡಿಗಳ ಸ್ವಚ್ಛಗೊಳಿಸುವಿಕೆ ಮತ್ತು ಸಣ್ಣ ದುರಸ್ತಿಗಳು.",
                    "android.resource://$pkg/drawable/img_drain", "", ""
                ),
                ProjectEntity(
                    "ID_TEMPLE", "Building the Temple", "ದೇವಸ್ಥಾನ ನಿರ್ಮಾಣ",
                    "Ramanagar Village", "ರಾಮನಗರ ಗ್ರಾಮ", "₹25,00,000", "2025-05-15",
                    "ONGOING", "ಪ್ರಗತಿಯಲ್ಲಿದೆ", 60,
                    "Construction of the new community temple and assembly hall.",
                    "ಹೊಸ ಸಮುದಾಯ ದೇವಸ್ಥಾನ ಮತ್ತು ಸಭಾಂಗಣದ ನಿರ್ಮಾಣ.",
                    "android.resource://$pkg/drawable/img_temple", "", ""
                ),
                ProjectEntity(
                    "ID_LIB", "Constructing Libraries", "ಗ್ರಂಥಾಲಯಗಳ ನಿರ್ಮಾಣ",
                    "Near High School", "ಪ್ರೌಢಶಾಲೆಯ ಹತ್ತಿರ", "₹18,00,000", "2025-08-15",
                    "PLANNED", "ಯೋಜನೆಯಲ್ಲಿದೆ", 5,
                    "Setting up a modern library with digital resources for students.",
                    "ವಿದ್ಯಾರ್ಥಿಗಳಿಗಾಗಿ ಡಿಜಿಟಲ್ ಸಂಪನ್ಮೂಲಗಳೊಂದಿಗೆ ಆಧುನಿಕ ಗ್ರಂಥಾಲಯವನ್ನು ಸ್ಥಾಪಿಸುವುದು.",
                    "android.resource://$pkg/drawable/img_library", "", ""
                ),
                ProjectEntity(
                    "ID_WATER", "Water Supply to Homes", "ಮನೆಗಳಿಗೆ ನೀರು ಸರಬರಾಜು",
                    "Entire Village", "ಇಡೀ ಗ್ರಾಮ", "₹45,00,000", "2025-06-30",
                    "ONGOING", "ಪ್ರಗತಿಯಲ್ಲಿದೆ", 85,
                    "Laying pipelines to provide individual tap connections to every household.",
                    "ಪ್ರತಿ ಮನೆಗೆ ಪ್ರತ್ಯೇಕ ನಳ ಸಂಪರ್ಕ ಒದಗಿಸಲು ಪೈಪ್ ಲೈನ್ ಅಳವಡಿಕೆ.",
                    "android.resource://$pkg/drawable/img_watersupply", "", ""
                )
            )
            repository.insertProjects(allProjects)
        }
    }
}
