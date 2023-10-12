package main.example.myapplication.adapter

import android.content.Context
import android.provider.CallLog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import main.example.myapplication.R
import main.example.myapplication.data.CallLogModel

class MyCallLogAdapter(val context: Context,val callLogList:List<CallLogModel>): RecyclerView.Adapter<MyCallLogAdapter.CallLogHolder>() {
    class CallLogHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        val name:TextView = itemView.findViewById(R.id.log_name)
        val number:TextView = itemView.findViewById(R.id.log_number)
        val dateTime:TextView = itemView.findViewById(R.id.log_dateTime)
        val duration:TextView = itemView.findViewById(R.id.log_duration)
        val type:TextView = itemView.findViewById(R.id.log_type)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.custom_calllog,parent,false)
        return CallLogHolder(root)
    }

    override fun getItemCount(): Int {
        return callLogList.size
    }

    override fun onBindViewHolder(holder: CallLogHolder, position: Int) {
        holder.name.text = "${callLogList[position].name}"
        holder.number.text = "${callLogList[position].number}"
        holder.dateTime.text = "${callLogList[position].dateTime}"
        holder.duration.text = "${callLogList[position].duration} second"

        val type = callLogList[position].type

        val typeText = when (type) {
            "1" -> "Incoming"
            "2" -> "Outgoing "
            "3" -> "Missed Called"
            "4" -> "Voice"
            "5" -> "Reject"
            "6" -> "Block"
            "7" -> "EXT"
            else -> "UNKNOWN"
        }

        holder.type.text = typeText
    }
}