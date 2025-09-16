/* eslint-disable no-console */
(function () {
  const fileInput = document.getElementById('fileInput');
  const dateInput = document.getElementById('dateInput');
  const delimiterSelect = document.getElementById('delimiter');
  const previewBtn = document.getElementById('previewBtn');
  const generateBtn = document.getElementById('generateBtn');
  const statusEl = document.getElementById('status');
  const previewEl = document.getElementById('preview');

  const colSelectIds = ['colDate', 'colScore', 'colQuantity', 'colPrice', 'colMethod'];
  const [colDate, colScore, colQuantity, colPrice, colMethod] = colSelectIds.map(id => document.getElementById(id));

  let header = [];
  let rows = [];

  function setStatus(message, kind) {
    statusEl.textContent = message || '';
    statusEl.className = 'status ' + (kind || '');
  }

  function safeTrim(value) {
    return typeof value === 'string' ? value.trim() : value;
  }

  function inferTodayDateString() {
    const now = new Date();
    const yyyy = now.getFullYear();
    const mm = String(now.getMonth() + 1).padStart(2, '0');
    const dd = String(now.getDate()).padStart(2, '0');
    return `${yyyy}-${mm}-${dd}`;
  }

  function yyyymmddhhmmss(now = new Date()) {
    const yyyy = now.getFullYear();
    const mm = String(now.getMonth() + 1).padStart(2, '0');
    const dd = String(now.getDate()).padStart(2, '0');
    const HH = String(now.getHours()).padStart(2, '0');
    const MM = String(now.getMinutes()).padStart(2, '0');
    const SS = String(now.getSeconds()).padStart(2, '0');
    return `${yyyy}${mm}${dd}${HH}${MM}${SS}`;
  }

  function readFileAsText(file) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = () => resolve(reader.result);
      reader.onerror = reject;
      reader.readAsText(file);
    });
  }

  function splitCsv(text, delimiter) {
    // Minimal CSV parser that supports quoted fields and delimiter/CRLF
    const rows = [];
    let field = '';
    let row = [];
    let inQuotes = false;
    for (let i = 0; i < text.length; i++) {
      const char = text[i];
      const next = text[i + 1];
      if (inQuotes) {
        if (char === '"' && next === '"') { // escaped quote
          field += '"';
          i++;
        } else if (char === '"') {
          inQuotes = false;
        } else {
          field += char;
        }
      } else {
        if (char === '"') {
          inQuotes = true;
        } else if (char === delimiter) {
          row.push(field);
          field = '';
        } else if (char === '\n') {
          row.push(field);
          rows.push(row);
          row = [];
          field = '';
        } else if (char === '\r') {
          // ignore, handle on \n
        } else {
          field += char;
        }
      }
    }
    // last field
    row.push(field);
    rows.push(row);
    // remove possible trailing empty line
    if (rows.length && rows[rows.length - 1].length === 1 && rows[rows.length - 1][0] === '') {
      rows.pop();
    }
    return rows;
  }

  function autoMapColumns(headers) {
    const normalized = headers.map(h => safeTrim(h || '').toLowerCase());
    const findIndex = (aliases) => {
      for (const alias of aliases) {
        const idx = normalized.indexOf(alias);
        if (idx !== -1) return idx;
      }
      return -1;
    };
    return {
      date: findIndex(['日期', 'date', '当天', '时间', '检测日期']),
      score: findIndex(['分数', 'score', '检测分数', '得分']),
      quantity: findIndex(['数量', 'qty', 'quantity', '数值', '数量值']),
      price: findIndex(['单价', '价格', 'price', 'unit price']),
      method: findIndex(['烹饪方式', '方式', 'method', 'cuisine']),
    };
  }

  function getSelectedMapping(headers) {
    const selects = [colDate, colScore, colQuantity, colPrice, colMethod];
    const keys = ['date', 'score', 'quantity', 'price', 'method'];
    const mapping = {};
    for (let i = 0; i < selects.length; i++) {
      const sel = selects[i];
      const value = sel && sel.value !== '' ? Number(sel.value) : -1;
      mapping[keys[i]] = value;
    }
    // Fill from auto mapping if not selected
    const auto = autoMapColumns(headers);
    for (const key of keys) {
      if (mapping[key] === -1) mapping[key] = auto[key];
    }
    return mapping;
  }

  function ensureMappingValid(mapping) {
    const missing = Object.entries(mapping).filter(([, idx]) => idx < 0).map(([k]) => k);
    if (missing.length) {
      throw new Error('未能识别列：' + missing.join('、') + '。请在“列名映射”中手动选择。');
    }
  }

  function normalizeDateToYMD(str) {
    const s = safeTrim(str || '');
    if (!s) return '';
    // Try common formats: YYYY-MM-DD, YYYY/MM/DD, YYYY.MM.DD, YYYYMMDD
    const m1 = s.match(/^(\d{4})[\-/\.](\d{1,2})[\-/\.]?(\d{1,2})$/);
    if (m1) {
      const yyyy = m1[1];
      const mm = String(Number(m1[2])).padStart(2, '0');
      const dd = String(Number(m1[3])).padStart(2, '0');
      return `${yyyy}-${mm}-${dd}`;
    }
    const m2 = s.match(/^(\d{4})(\d{2})(\d{2})$/);
    if (m2) return `${m2[1]}-${m2[2]}-${m2[3]}`;
    // Fallback: try Date
    const d = new Date(s);
    if (!isNaN(d.getTime())) {
      const yyyy = d.getFullYear();
      const mm = String(d.getMonth() + 1).padStart(2, '0');
      const dd = String(d.getDate()).padStart(2, '0');
      return `${yyyy}-${mm}-${dd}`;
    }
    return s;
  }

  function toNumber(value) {
    if (typeof value === 'number') return value;
    const s = String(value ?? '').replace(/[,\s]/g, '');
    const n = Number(s);
    return isNaN(n) ? NaN : n;
  }

  function filterRows(header, rows, mapping, targetDateYMD) {
    const idxDate = mapping.date;
    const idxScore = mapping.score;
    const idxQty = mapping.quantity;
    const idxPrice = mapping.price;
    const idxMethod = mapping.method;
    const result = [];
    for (const r of rows) {
      const dateYMD = normalizeDateToYMD(r[idxDate]);
      if (dateYMD !== targetDateYMD) continue;
      const score = toNumber(r[idxScore]);
      const qty = toNumber(r[idxQty]);
      const price = toNumber(r[idxPrice]);
      const method = safeTrim(r[idxMethod] || '').toLowerCase();
      if (!(score < 70)) continue;
      if (!(qty === 200)) continue;
      if (!(price === 10)) continue;
      if (!(method === '西餐' || method === 'western' || method === 'western food')) continue;
      result.push(r);
    }
    return result;
  }

  function arraysToCsv(rows, delimiter) {
    return rows.map(row => row.map(cell => {
      const v = cell == null ? '' : String(cell);
      const needQuote = v.includes('"') || v.includes('\n') || v.includes('\r') || v.includes(delimiter);
      if (needQuote) {
        return '"' + v.replace(/"/g, '""') + '"';
      }
      return v;
    }).join(delimiter)).join('\n');
  }

  function downloadCsv(filename, text) {
    const blob = new Blob([text], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    URL.revokeObjectURL(url);
  }

  function populateMappingSelects(headers) {
    const selects = [colDate, colScore, colQuantity, colPrice, colMethod];
    for (const sel of selects) {
      sel.innerHTML = '';
      const optEmpty = document.createElement('option');
      optEmpty.value = '';
      optEmpty.textContent = '自动匹配';
      sel.appendChild(optEmpty);
      headers.forEach((h, idx) => {
        const opt = document.createElement('option');
        opt.value = String(idx);
        opt.textContent = h;
        sel.appendChild(opt);
      });
    }
  }

  async function handleFileChange() {
    const file = fileInput.files && fileInput.files[0];
    previewEl.innerHTML = '';
    setStatus('', '');
    if (!file) {
      previewBtn.disabled = true;
      generateBtn.disabled = true;
      return;
    }
    setStatus('读取文件中…');
    try {
      const delimiter = delimiterSelect.value === '\t' ? '\t' : delimiterSelect.value;
      const text = await readFileAsText(file);
      const grid = splitCsv(text, delimiter);
      if (!grid.length) throw new Error('文件为空');
      header = grid[0].map(safeTrim);
      rows = grid.slice(1);
      populateMappingSelects(header);
      previewBtn.disabled = false;
      generateBtn.disabled = false;
      setStatus(`已加载：${file.name}（${rows.length} 行数据）`, 'success');
    } catch (err) {
      console.error(err);
      previewBtn.disabled = true;
      generateBtn.disabled = true;
      setStatus('读取失败：' + (err && err.message ? err.message : String(err)), 'error');
    }
  }

  function renderPreview(limit = 20) {
    if (!header.length) return;
    const table = document.createElement('table');
    const thead = document.createElement('thead');
    const trh = document.createElement('tr');
    header.forEach(h => {
      const th = document.createElement('th');
      th.textContent = h;
      trh.appendChild(th);
    });
    thead.appendChild(trh);
    table.appendChild(thead);
    const tbody = document.createElement('tbody');
    rows.slice(0, limit).forEach(r => {
      const tr = document.createElement('tr');
      header.forEach((_, idx) => {
        const td = document.createElement('td');
        td.textContent = r[idx] ?? '';
        tr.appendChild(td);
      });
      tbody.appendChild(tr);
    });
    table.appendChild(tbody);
    previewEl.innerHTML = '';
    previewEl.appendChild(table);
  }

  function onGenerate() {
    if (!header.length) return;
    const mapping = getSelectedMapping(header);
    try {
      ensureMappingValid(mapping);
    } catch (e) {
      setStatus(e.message, 'error');
      return;
    }
    const targetDate = dateInput.value || inferTodayDateString();
    const targetYMD = normalizeDateToYMD(targetDate);
    setStatus('筛选与导出中…');
    const filtered = filterRows(header, rows, mapping, targetYMD);
    const output = [header, ...filtered];
    const delimiter = delimiterSelect.value === '\t' ? '\t' : delimiterSelect.value;
    const csvText = arraysToCsv(output, delimiter);
    const filename = `${yyyymmddhhmmss()}_result.csv`;
    downloadCsv(filename, csvText);
    setStatus(`已生成：${filename}（${filtered.length} 行）`, 'success');
  }

  fileInput.addEventListener('change', handleFileChange);
  previewBtn.addEventListener('click', () => renderPreview());
  generateBtn.addEventListener('click', onGenerate);
})();

