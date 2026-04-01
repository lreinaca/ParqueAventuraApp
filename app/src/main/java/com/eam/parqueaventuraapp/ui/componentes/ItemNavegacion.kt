package com.eam.parqueaventuraapp.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eam.parqueaventuraapp.ui.theme.*

@Composable
fun ItemNavegacion(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    val color = if (isSelected) VerdeAccent else Color.Gray

    val fondoModifier =
        if (isSelected)
            Modifier
                .background(Color(0xFFE8F5E9), RoundedCornerShape(12.dp))
        else
            Modifier

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = fondoModifier
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {

        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(22.dp)
        )

        Text(
            text = label,
            fontSize = 10.sp, //sp = scale-independent pixel
            color = color,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}