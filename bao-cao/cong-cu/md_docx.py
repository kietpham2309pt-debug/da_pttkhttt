import io
import re

from docx import Document
from docx.enum.section import WD_ORIENT, WD_SECTION
from docx.enum.table import WD_TABLE_ALIGNMENT
from docx.enum.text import WD_ALIGN_PARAGRAPH, WD_BREAK
from docx.oxml import OxmlElement
from docx.oxml.ns import qn
from docx.shared import Cm, Pt, RGBColor

FONT = "Times New Roman"
MAU_TIEU_DE = RGBColor(0x1F, 0x38, 0x64)
NEN_DAU_BANG = "D9E2F3"
RONG_TRANG_CM = 16.0


def dat_font(run, co=None, dam=None, nghieng=None, mau=None):
    run.font.name = FONT
    rpr = run._element.get_or_add_rPr()
    rfonts = rpr.find(qn("w:rFonts"))
    if rfonts is None:
        rfonts = OxmlElement("w:rFonts")
        rpr.insert(0, rfonts)
    for k in ("w:ascii", "w:hAnsi", "w:cs", "w:eastAsia"):
        rfonts.set(qn(k), FONT)
    if co:
        run.font.size = Pt(co)
    if dam is not None:
        run.bold = dam
    if nghieng is not None:
        run.italic = nghieng
    if mau is not None:
        run.font.color.rgb = mau


def kieu_font(style, co, dam=None, nghieng=None, mau=None):
    style.font.name = FONT
    style.font.size = Pt(co)
    if dam is not None:
        style.font.bold = dam
    if nghieng is not None:
        style.font.italic = nghieng
    if mau is not None:
        style.font.color.rgb = mau
    rpr = style.element.get_or_add_rPr()
    rfonts = rpr.find(qn("w:rFonts"))
    if rfonts is None:
        rfonts = OxmlElement("w:rFonts")
        rpr.insert(0, rfonts)
    for k in ("w:ascii", "w:hAnsi", "w:cs", "w:eastAsia"):
        rfonts.set(qn(k), FONT)
    for k in ("w:asciiTheme", "w:hAnsiTheme", "w:cstheme", "w:eastAsiaTheme"):
        if rfonts.get(qn(k)) is not None:
            del rfonts.attrib[qn(k)]


def le_trang(s):
    s.left_margin, s.right_margin = Cm(3), Cm(2)
    s.top_margin, s.bottom_margin = Cm(2), Cm(2)


def tao_tai_lieu():
    d = Document()
    s = d.sections[0]
    s.page_width, s.page_height = Cm(21), Cm(29.7)
    s.orientation = WD_ORIENT.PORTRAIT
    le_trang(s)
    n = d.styles["Normal"]
    kieu_font(n, 13)
    n.paragraph_format.space_after = Pt(4)
    n.paragraph_format.line_spacing = 1.2
    for ten, co, truoc in (("Heading 1", 16, 18), ("Heading 2", 14, 12), ("Heading 3", 13, 10)):
        st = d.styles[ten]
        kieu_font(st, co, dam=True, nghieng=False, mau=MAU_TIEU_DE)
        st.paragraph_format.space_before = Pt(truoc)
        st.paragraph_format.space_after = Pt(6)
        st.paragraph_format.keep_with_next = True
    so_trang(s)
    return d


def so_trang(section):
    p = section.footer.paragraphs[0]
    p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r = p.add_run()
    dat_font(r, 11)
    for loai, chu in (("begin", None), (None, "PAGE"), ("end", None)):
        if loai:
            f = OxmlElement("w:fldChar")
            f.set(qn("w:fldCharType"), loai)
            r._element.append(f)
        else:
            t = OxmlElement("w:instrText")
            t.set(qn("xml:space"), "preserve")
            t.text = chu
            r._element.append(t)


MAU_INLINE = re.compile(r"(\*\*[^*]+\*\*|\*[^*\s][^*]*\*|`[^`]+`|\[[^\]]+\]\([^)]+\)|<u>[^<]+</u>)")


def them_chu(p, text, co=None, dam=False, nghieng=False):
    for phan in MAU_INLINE.split(text):
        if not phan:
            continue
        b, i, u = dam, nghieng, False
        if phan.startswith("**") and phan.endswith("**") and len(phan) > 4:
            phan, b = phan[2:-2], True
        elif phan.startswith("<u>") and phan.endswith("</u>"):
            phan, u = phan[3:-4], True
        elif phan.startswith("*") and phan.endswith("*") and len(phan) > 2:
            phan, i = phan[1:-1], True
        elif phan.startswith("`") and phan.endswith("`"):
            phan = phan[1:-1]
        elif phan.startswith("["):
            m = re.match(r"\[([^\]]+)\]\(([^)]+)\)", phan)
            phan = m.group(1).strip("`")
        phan = phan.replace("&lt;", "<").replace("&gt;", ">")
        r = p.add_run(phan)
        dat_font(r, co, b, i)
        if u:
            r.underline = True
    return p


def doan(d, text, can=None, co=None, dam=False, nghieng=False, sau=None, thut=None):
    p = d.add_paragraph()
    them_chu(p, text, co, dam, nghieng)
    if can is not None:
        p.alignment = can
    if sau is not None:
        p.paragraph_format.space_after = Pt(sau)
    if thut is not None:
        p.paragraph_format.left_indent = Cm(thut)
    return p


def gach_dau(d, text, co=None):
    p = doan(d, "• " + text, thut=0.6, co=co)
    p.paragraph_format.first_line_indent = Cm(-0.45)
    return p


def tieu_de(d, text, muc):
    h = d.add_heading(level=muc)
    r = h.add_run(text)
    dat_font(r)
    return h


def nen_o(cell, mau):
    tcpr = cell._tc.get_or_add_tcPr()
    sh = OxmlElement("w:shd")
    sh.set(qn("w:val"), "clear")
    sh.set(qn("w:color"), "auto")
    sh.set(qn("w:fill"), mau)
    tcpr.append(sh)


def lap_lai_dau(row):
    trpr = row._tr.get_or_add_trPr()
    e = OxmlElement("w:tblHeader")
    e.set(qn("w:val"), "true")
    trpr.append(e)


def ghi_o(cell, text, co=11.5, dam=False, can=None):
    cell.text = ""
    dong = text.split("<br>") if "<br>" in text else [text]
    for k, t in enumerate(dong):
        p = cell.paragraphs[0] if k == 0 else cell.add_paragraph()
        p.paragraph_format.space_after = Pt(1)
        p.paragraph_format.line_spacing = 1.1
        if can is not None:
            p.alignment = can
        them_chu(p, t.strip(), co, dam)


def co_dinh(t, rong, tong_cm=None):
    tong_cm = tong_cm or RONG_TRANG_CM
    t.autofit = False
    tblpr = t._tbl.tblPr
    lay = OxmlElement("w:tblLayout")
    lay.set(qn("w:type"), "fixed")
    tblpr.append(lay)
    tong = sum(rong)
    ws = [Cm(tong_cm * x / tong) for x in rong]
    grid = t._tbl.tblGrid
    for i, gc in enumerate(grid.findall(qn("w:gridCol"))):
        gc.set(qn("w:w"), str(int(ws[i].twips)))
    for row in t.rows:
        for i, c in enumerate(row.cells):
            c.width = ws[i]


def bang(d, dau, dong, rong=None, co=11.5):
    t = d.add_table(rows=1, cols=len(dau))
    t.style = "Table Grid"
    t.alignment = WD_TABLE_ALIGNMENT.CENTER
    for i, h in enumerate(dau):
        c = t.rows[0].cells[i]
        ghi_o(c, h, co, True, WD_ALIGN_PARAGRAPH.CENTER)
        nen_o(c, NEN_DAU_BANG)
    lap_lai_dau(t.rows[0])
    for r in dong:
        cells = t.add_row().cells
        for i in range(len(dau)):
            ghi_o(cells[i], r[i] if i < len(r) else "", co)
    if not rong:
        rong = [max(len(h), 4) for h in dau]
        for r in dong:
            for i in range(len(dau)):
                if i < len(r):
                    rong[i] = max(rong[i], min(len(r[i]), 60))
    co_dinh(t, rong)
    d.add_paragraph().paragraph_format.space_after = Pt(2)
    return t


class DemHinh:
    def __init__(self):
        self.so = 0
        self.ds = []


def hinh(d, duong, chu_thich, dem, rong_cm=15.0, cao_toi_da_cm=22.0):
    from PIL import Image
    w, h = Image.open(duong).size
    rong = min(rong_cm, RONG_TRANG_CM)
    if h / w * rong > cao_toi_da_cm:
        rong = cao_toi_da_cm * w / h
    p = d.add_paragraph()
    p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p.paragraph_format.keep_with_next = True
    p.add_run().add_picture(duong, width=Cm(rong))
    dem.so += 1
    dem.ds.append(chu_thich)
    c = d.add_paragraph()
    c.alignment = WD_ALIGN_PARAGRAPH.CENTER
    them_chu(c, f"Hình {dem.so}. {chu_thich}", 12, nghieng=True)
    return dem.so


def trang_ngang(d, ham):
    s = d.add_section(WD_SECTION.NEW_PAGE)
    s.orientation = WD_ORIENT.LANDSCAPE
    s.page_width, s.page_height = Cm(29.7), Cm(21)
    le_trang(s)
    ham()
    s2 = d.add_section(WD_SECTION.NEW_PAGE)
    s2.orientation = WD_ORIENT.PORTRAIT
    s2.page_width, s2.page_height = Cm(21), Cm(29.7)
    le_trang(s2)


def hop_mau1(d, khoi_ds):
    t = d.add_table(rows=1, cols=1)
    t.style = "Table Grid"
    t.alignment = WD_TABLE_ALIGNMENT.CENTER
    co_dinh(t, [1])
    c = t.rows[0].cells[0]
    nen_o(c, "F7F9FC")
    dau_tien = True
    for loai, noi_dung in khoi_ds:
        p = c.paragraphs[0] if dau_tien else c.add_paragraph()
        dau_tien = False
        p.paragraph_format.space_after = Pt(3)
        p.paragraph_format.line_spacing = 1.15
        if loai == "ol":
            so, text = noi_dung
            p.paragraph_format.left_indent = Cm(0.9)
            p.paragraph_format.first_line_indent = Cm(-0.6)
            them_chu(p, f"{so}. {text}", 12.5)
        elif loai == "ul":
            p.paragraph_format.left_indent = Cm(0.9)
            p.paragraph_format.first_line_indent = Cm(-0.5)
            them_chu(p, "• " + noi_dung, 12.5)
        else:
            them_chu(p, noi_dung, 12.5)
    d.add_paragraph().paragraph_format.space_after = Pt(2)


def doc_md(duong):
    return io.open(duong, encoding="utf-8").read().replace("\r\n", "\n")


def chia_muc(text, cap="## "):
    muc, ten, dong = [], None, []
    for l in text.split("\n"):
        if l.startswith(cap) and not l.startswith(cap + "#"):
            if ten is not None:
                muc.append((ten, dong))
            ten, dong = l[len(cap):].strip(), []
        elif ten is not None:
            dong.append(l)
    if ten is not None:
        muc.append((ten, dong))
    return muc


def tach_hang(l):
    s = l.strip()
    if s.startswith("|"):
        s = s[1:]
    if s.endswith("|"):
        s = s[:-1]
    o, cur, trong_ma = [], "", False
    for ch in s:
        if ch == "`":
            trong_ma = not trong_ma
        if ch == "|" and not trong_ma:
            o.append(cur.strip())
            cur = ""
        else:
            cur += ch
    o.append(cur.strip())
    return o


def la_phan_cach(l):
    return bool(re.match(r"^\s*\|?\s*:?-{2,}", l)) and set(l.replace("|", "").replace(":", "").replace("-", "").strip()) == set()


def khoi(dong):
    i, out = 0, []
    while i < len(dong):
        l = dong[i]
        s = l.strip()
        if not s:
            i += 1
            continue
        if s.startswith(">"):
            q = []
            while i < len(dong) and dong[i].strip().startswith(">"):
                q.append(re.sub(r"^\s*>\s?", "", dong[i]))
                i += 1
            out.append(("quote", q))
            continue
        if s.startswith("|"):
            rows = []
            while i < len(dong) and dong[i].strip().startswith("|"):
                if not la_phan_cach(dong[i]):
                    rows.append(tach_hang(dong[i]))
                i += 1
            out.append(("table", rows))
            continue
        m = re.match(r"^!\[([^\]]*)\]\(([^)]+)\)", s)
        if m:
            out.append(("img", (m.group(1), m.group(2))))
            i += 1
            continue
        if s.startswith("### "):
            out.append(("h3", s[4:].strip()))
            i += 1
            continue
        if s.startswith("#"):
            i += 1
            continue
        m = re.match(r"^(\d+)\.\s+(.*)", s)
        if m:
            out.append(("ol", (m.group(1), m.group(2))))
            i += 1
            continue
        if s.startswith("- ") or s.startswith("* "):
            text = s[2:]
            i += 1
            while i < len(dong) and dong[i].startswith("  ") and dong[i].strip() and not dong[i].strip().startswith(("- ", "|", ">")):
                text += " " + dong[i].strip()
                i += 1
            out.append(("ul", text))
            continue
        text = s
        i += 1
        while i < len(dong) and dong[i].strip() and not re.match(r"^\s*(>|\||!\[|#|\d+\.\s|- |\* )", dong[i]):
            text += " " + dong[i].strip()
            i += 1
        out.append(("p", text))
    return out


def khoi_trich(q):
    return [k for k in khoi(q) if k[0] in ("ol", "ul", "p")]


def ve_khoi(d, ds, rong_bang=None):
    for loai, nd in ds:
        if loai == "quote":
            hop_mau1(d, khoi_trich(nd))
        elif loai == "table":
            if not nd:
                continue
            dau, than = nd[0], nd[1:]
            if len(dau) == 2 and dau[0].lower() in ("mục",):
                bang(d, dau, than, rong=[1, 3.2])
            elif len(dau) == 3 and dau[0].lower() == "bước":
                bang(d, dau, than, rong=[0.8, 3, 4])
            else:
                bang(d, dau, than, rong=rong_bang)
        elif loai == "img":
            continue
        elif loai == "h3":
            doan(d, nd, dam=True)
        elif loai == "ol":
            p = doan(d, f"{nd[0]}. {nd[1]}", thut=0.6)
            p.paragraph_format.first_line_indent = Cm(-0.5)
        elif loai == "ul":
            gach_dau(d, nd)
        else:
            doan(d, nd)


def muc_luc(d):
    p = d.add_paragraph()
    r = p.add_run()
    dat_font(r)
    f1 = OxmlElement("w:fldChar")
    f1.set(qn("w:fldCharType"), "begin")
    t = OxmlElement("w:instrText")
    t.set(qn("xml:space"), "preserve")
    t.text = 'TOC \\o "1-3" \\h \\z \\u'
    f2 = OxmlElement("w:fldChar")
    f2.set(qn("w:fldCharType"), "separate")
    t2 = OxmlElement("w:t")
    t2.text = "Mục lục sẽ hiện sau khi cập nhật trường (chuột phải, Update Field)."
    f3 = OxmlElement("w:fldChar")
    f3.set(qn("w:fldCharType"), "end")
    for e in (f1, t, f2, t2, f3):
        r._element.append(e)


def ngat_trang(d):
    d.add_paragraph().add_run().add_break(WD_BREAK.PAGE)
