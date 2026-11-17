import os
import sys

import win32com.client

docx = os.path.abspath(sys.argv[1])
pdf = os.path.abspath(sys.argv[2])
w = win32com.client.DispatchEx("Word.Application")
w.Visible = False
w.DisplayAlerts = 0
try:
    doc = w.Documents.Open(docx)
    if doc.TablesOfContents.Count > 0:
        doc.TablesOfContents(1).Update()
    doc.Fields.Update()
    if doc.TablesOfContents.Count > 0:
        doc.TablesOfContents(1).Update()
    doc.Save()
    doc.ExportAsFixedFormat(pdf, 17)
    print("trang:", doc.ComputeStatistics(2))
    doc.Close(0)
finally:
    w.Quit()
