package com.connect.eduhubconnect.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.connect.eduhubconnect.R
import com.connect.eduhubconnect.dto.ExperienceDTO

class ExperienceAdapter(private val experienceList: List<ExperienceDTO>) :
    RecyclerView.Adapter<ExperienceAdapter.ExperienceViewHolder>() {

    class ExperienceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val positionText: TextView = itemView.findViewById(R.id.experienceTitle)
        val companyText: TextView = itemView.findViewById(R.id.experienceCompany)
        val durationText: TextView = itemView.findViewById(R.id.experienceDates)
        val descriptionText: TextView = itemView.findViewById(R.id.experienceDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExperienceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_experience, parent, false)
        return ExperienceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExperienceViewHolder, position: Int) {
        val item = experienceList[position]
        holder.positionText.text = item.jobTitle
        holder.companyText.text = item.companyName
        if(item.endDate!=null){
            holder.durationText.text = " ${item.startDate} ${item.endDate}"
        }else{
            holder.durationText.text = " ${item.startDate} - Present"
        }
        holder.descriptionText.text = item.description
    }

    override fun getItemCount(): Int = experienceList.size
}
