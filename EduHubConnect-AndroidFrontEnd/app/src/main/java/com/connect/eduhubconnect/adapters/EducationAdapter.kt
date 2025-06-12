package com.connect.eduhubconnect.adapters

// EducationAdapter.kt
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.connect.eduhubconnect.R
import com.connect.eduhubconnect.dto.EducationDTO

class EducationAdapter(private val educationList: List<EducationDTO>) :
    RecyclerView.Adapter<EducationAdapter.EducationViewHolder>() {

    class EducationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val degreeText: TextView = itemView.findViewById(R.id.educationDegree)
        val institutionText: TextView = itemView.findViewById(R.id.educationInstitution)
        val yearText: TextView = itemView.findViewById(R.id.educationDates)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EducationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_education, parent, false)
        return EducationViewHolder(view)
    }

    override fun onBindViewHolder(holder: EducationViewHolder, position: Int) {
        val item = educationList[position]
        holder.degreeText.text = item.degree
        holder.institutionText.text = item.institution
        holder.yearText.text = item.graduationYear
    }

    override fun getItemCount(): Int = educationList.size
}
