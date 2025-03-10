package com.example.buynow.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.buynow.R
import com.example.buynow.data.model.ShippingAddress

class AddressAdapter : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    private var addresses = mutableListOf<ShippingAddress>()
    private var onDeleteClickListener: ((ShippingAddress) -> Unit)? = null

    fun setOnDeleteClickListener(listener: (ShippingAddress) -> Unit) {
        onDeleteClickListener = listener
    }

    fun updateAddresses(newAddresses: List<ShippingAddress>) {
        addresses.clear()
        addresses.addAll(newAddresses)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_address, parent, false)
        return AddressViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.bind(addresses[position], onDeleteClickListener)
    }

    override fun getItemCount() = addresses.size

    class AddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val fullNameTv: TextView = itemView.findViewById(R.id.fullNameTv)
        private val phoneNumberTv: TextView = itemView.findViewById(R.id.phoneNumberTv)
        private val addressTv: TextView = itemView.findViewById(R.id.addressDetailsTv)
        private val deleteBtn: ImageButton = itemView.findViewById(R.id.deleteAddressBtn)

        fun bind(address: ShippingAddress, onDeleteClick: ((ShippingAddress) -> Unit)?) {
            fullNameTv.text = address.fullName
            phoneNumberTv.text = address.phoneNumber
            addressTv.text = "${address.streetAddress}, ${address.city}, ${address.state} - ${address.zipCode}"

            deleteBtn.setOnClickListener {
                onDeleteClick?.invoke(address)
            }
        }
    }
}