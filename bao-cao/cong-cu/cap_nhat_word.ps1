param([string]$Docx, [string]$Pdf)
$w = New-Object -ComObject Word.Application
$w.Visible = $false
$w.DisplayAlerts = 0
try {
    $doc = $w.Documents.Open($Docx)
    if ($doc.TablesOfContents.Count -gt 0) { $doc.TablesOfContents.Item(1).Update() }
    $null = $doc.Fields.Update()
    if ($doc.TablesOfContents.Count -gt 0) { $doc.TablesOfContents.Item(1).Update() }
    $doc.Save()
    $doc.ExportAsFixedFormat($Pdf, 17)
    "trang: " + $doc.ComputeStatistics(2)
    $doc.Close(0)
}
finally {
    if ($w.Documents.Count -eq 0) { $w.Quit() }
}
